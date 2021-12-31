/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.PropertyBucket;
import io.openapiparser.support.Strings;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * resolves all $ref'erences of the base document loading all referenced documents.
 */
public class ReferenceResolver {
    private static final String HASH = "#";

    private final URI baseUri;
    private final Reader reader;
    private final Converter converter;
    private final ReferenceRegistry references;
    private final DocumentRegistry documents = new DocumentRegistry ();

    private PropertyBucket object;

    public ReferenceResolver (URI baseUri, Reader reader, Converter converter, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.reader = reader;
        this.converter = converter;
        this.references = references;
        this.object = PropertyBucket.empty ();
    }

    public void resolve () throws ResolverException {
        initBaseDocument ();
        collectReferences ();
        resolveReferences ();
    }

    // todo throw if missing
    public Reference resolve(URI baseUri, String ref) {
        if (ref.startsWith (HASH)) {
            // same document
            return references.getRef(baseUri.resolve (ref));
        } else {
            // other document
            if (ref.contains (HASH)) {
                // with path fragment
                final int idxHash = ref.indexOf (HASH);
                String document = ref.substring (0, idxHash);
                String fragment = ref.substring (idxHash);
                URI documentUri = baseUri.resolve (document);
                URI refUri = documentUri.resolve (fragment);
                return references.getRef(refUri);
            } else {
                // full document
                URI refUri = baseUri.resolve (ref);
                return references.getRef(refUri);
            }
        }
    }

    /**
     * get the root {@link PropertyBucket} of the document. The bucket is empty if the document was
     * not {@link #resolve()}d.
     *
     * @return root property bucket.
     */
    public PropertyBucket getRootObject () {
        return object;
    }

    private void initBaseDocument () throws ResolverException {
        final Map<String, Object> document = loadDocument (baseUri);
        object = new PropertyBucket (baseUri, document);
        documents.add (baseUri, object);
    }

    private void collectReferences () throws ResolverException {
        collectReferences (baseUri, object);
    }

    private void resolveReferences() {
        references.resolve((documentUri, ref) -> {
            final PropertyBucket document = documents.get (documentUri);
            return document.findProperty (JsonPointer.fromFragment (ref));

//            String fragment = ref.substring(ref.indexOf (HASH));
//            return getProperties (document, fragment);

//            Node documentNode = documents.get (documentUri);
//            String fragment = ref.substring(ref.indexOf (HASH));
//            return getRawRefNode (documentNode, fragment);
        });
    }

    private void collectReferences (URI uri, PropertyBucket properties) throws ResolverException {
        properties.forEach((name, value) -> {
            if (name.equals (Keywords.REF)) {
                String ref = getRef (uri, name, value);

                if (ref.startsWith (HASH)) {
                    // into same document
                    references.add (uri, uri, ref);
                } else {
                    // into other document
                    if (ref.contains (HASH)) {
                        String document = ref.substring (0, ref.indexOf (HASH));
                        URI documentUri = uri.resolve (document);

                        if (!documents.contains (documentUri)) {
                            PropertyBucket documentProperties = new PropertyBucket (
                                documentUri, loadDocument (documentUri)
                            );

                            documents.add (documentUri, documentProperties);
                            collectReferences (documentUri, documentProperties);
                        }

                        references.add (baseUri, documentUri, ref);
                    } else {
                        // is other document
                    }
                }
            } else {
                // todo correct uri?
                properties.traverseProperty (name, props -> {
                    collectReferences (uri, props);
                });
            }
        });
    }

    private String getRef (URI uri, String name, Object value) {
        // todo path, JsonPointer?
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
