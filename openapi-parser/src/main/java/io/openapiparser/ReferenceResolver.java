/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.Bucket;
import io.openapiparser.support.Strings;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * resolves all $ref's of the base document loading all referenced documents.
 */
public class ReferenceResolver {
    private static final String HASH = "#";

    private final URI baseUri;
    private final Reader reader;
    private final Converter converter;
    private final ReferenceRegistry references;
    private final DocumentRegistry documents = new DocumentRegistry ();

    private Bucket object;

    public ReferenceResolver (URI baseUri, Reader reader, Converter converter, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.reader = reader;
        this.converter = converter;
        this.references = references;
        this.object = Bucket.empty ();
    }

    // todo could return object ????
    public void resolve () throws ResolverException {
        initBaseDocument ();
        collectReferences ();
        resolveReferences ();
    }

    // todo throw if missing
    public Reference resolve(URI baseUri, String ref) {
        String encodedRef = ref
            .replace ("{", "%7B")
            .replace ("}", "%7D");

        if (encodedRef.startsWith (HASH)) {
            // same document
            return references.getRef(baseUri.resolve (encodedRef));
        } else {
            // other document
            if (encodedRef.contains (HASH)) {
                // with path fragment
                final int idxHash = encodedRef.indexOf (HASH);
                String document = encodedRef.substring (0, idxHash);
                String fragment = encodedRef.substring (idxHash);
                URI documentUri = baseUri.resolve (document);
                URI refUri = documentUri.resolve (fragment);
                return references.getRef(refUri);
            } else {
                // full document
                URI refUri = baseUri.resolve (encodedRef);
                return references.getRef(refUri);
            }
        }
    }

    /**
     * get the root {@link Bucket} of the document. The bucket is empty if the document was
     * not {@link #resolve()}d.
     *
     * @return root property bucket.
     */
    public Bucket getObject () {
        return object;
    }

    private void initBaseDocument () throws ResolverException {
        final Map<String, Object> document = loadDocument (baseUri);
        object = new Bucket (baseUri, document);
        documents.add (baseUri, object);
    }

    private void collectReferences () throws ResolverException {
        collectReferences (baseUri, object);
    }

    private void resolveReferences() {
        references.resolve((documentUri, ref) -> {
            Bucket document = documents.get (documentUri);
            if (ref.contains ("#")) {
                String fragment = ref.substring(ref.indexOf (HASH));
                return document.getProperty (JsonPointer.fromFragment (fragment));
            } else {
                return document.getRawValues ();
            }
        });
    }

    private void collectReferences (URI uri, Bucket properties) throws ResolverException {
        properties.forEach((name, value) -> {
            if (name.equals (Keywords.REF)) {
                String ref = getRef (uri, name, value);

                if (ref.startsWith (HASH)) {
                    // into same document
                    references.add (uri, uri, ref);
                } else {
                    // into other document
                    if (ref.contains (HASH)) {
                        // with pointer
                        String documentName = ref.substring (0, ref.indexOf (HASH));
                        URI documentUri = uri.resolve (documentName);

                        if (!documents.contains (documentUri)) {
                            Bucket document = new Bucket (
                                documentUri,
                                loadDocument (documentUri));

                            documents.add (documentUri, document);
                            collectReferences (documentUri, document);
                        }

                        references.add (baseUri, documentUri, ref);
                    } else {
                        // full document
                        URI documentUri = uri.resolve (ref);

                        if (!documents.contains (documentUri)) {
                            Bucket document = new Bucket (
                                documentUri,
                                null,
                                loadDocument (documentUri));

                            documents.add (documentUri, document);
                            collectReferences (documentUri, document);
                        }

                        references.add (baseUri, documentUri, ref);
                    }
                }
            } else {
                properties.walkPropertyTree (name, props -> {
                    collectReferences (properties.getSource (), props);
                });
            }
        });
    }

    private String getRef (URI uri, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("unable to resolve empty $ref in %s.", uri));
        }
        return ref;
    }

    @SuppressWarnings ("unchecked")
    private Map<String, Object> loadDocument (URI documentUri) throws ResolverException {
        try {
            return (Map<String, Object>) converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve %s.", documentUri), e);
        }
    }
}
