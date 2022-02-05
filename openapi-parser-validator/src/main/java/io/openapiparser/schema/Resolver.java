/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.Converter;
import io.openapiparser.Reader;
import io.openapiparser.support.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.converter.Types.convertOrNull;

/**
 * loads the base document and resolves all internal and external $ref's. In case of an external
 * $ref it automatically downloads the referenced document.
 */
public class Resolver {
    private static final String HASH = "#";

    private final Reader reader;
    private final Converter converter;
    private final DocumentStore documents;

    public Resolver (Reader reader, Converter converter, DocumentStore documents) {
        this.reader = reader;
        this.converter = converter;
        this.documents = documents;
    }

    public ResolverResult resolve (URI uri) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        Object document = loadDocument (uri);
        documents.add (uri, document);

        collectReferences (uri, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (uri, document, registry);
    }

    public ResolverResult resolve (String resourcePath) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        URI uri = URI.create (resourcePath);
        Object document = loadDocument (resourcePath);
        documents.add (uri, document);

        collectReferences (uri, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (uri, document, registry);
    }

    /**
     * resolves a given {@code document}. It will load any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of any ref.
     *
     * @param uri document uri
     * @param document document content
     * @return resolver result
     */
    public ResolverResult resolve (URI uri, Object document) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        documents.add (uri, document);

        collectReferences (uri, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (uri, document, registry);
    }

//    public Object resolve (URI uri) throws ResolverException {
//        Object document = initDocument (uri);
//        collectReferences (uri, uri, document);
        // resolve

//        if (!(document instanceof Map)) {
//            return document;
//        }
//
//        Bucket bucket = new Bucket (uri, asMap (document));
//        collectReferences (uri, bucket);
//    }

    // todo throw if missing
    /*
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
    }*/

    /**
     * get the root {@link Bucket} of the document. The bucket is empty if the document was
     * not {@link #resolve()}d.
     *
     * @return root property bucket.
     */
//    public Bucket getObject () {
//        return object;
//    }

//    private Object initDocument (URI uri) throws ResolverException {
//        Object document = loadDocument (uri);
//        documents.add (uri, document);
//        return document;
//    }

    private void collectReferences (
        URI base,
        URI uri,
        Object document,
        ReferenceRegistry registry) throws ResolverException {

        Bucket bucket = toBucket (uri, document);
        if (bucket == null)
            return;

        collectReferences (base, uri, bucket, registry);
    }

    private @Nullable Bucket toBucket(URI uri, Object source) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (uri, asMap (source));
    }

    private void resolveReferences (ReferenceRegistry references) {
        references.resolve((documentUri, ref) -> {
            Object o = documents.get (documentUri);
            Bucket document = toBucket (documentUri, o);
            if (document == null)
                return o;

            if (ref.contains ("#")) {
                String fragment = ref.substring(ref.indexOf (HASH));
                final Object property = document.getRawValue (JsonPointer.fromFragment (fragment));
                if (property == null) {
                    throw new ResolverException (
                        String.format ("failed to resolve ref %s/%s.", documentUri, ref));
                }
                return property;
            } else {
                return document.getRawValues ();
            }
        });
    }

    private void collectReferences (
        URI baseUri,
        URI uri,
        Bucket bucket,
        ReferenceRegistry references) throws ResolverException {

        bucket.forEach((name, value) -> {
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
                            Object document = loadDocument (documentUri);
                            documents.add (documentUri, document);
                            collectReferences (baseUri, documentUri, document, references);
                        }

                        references.add (baseUri, documentUri, ref);
                    } else {
                        // full document
                        URI documentUri = uri.resolve (ref);

                        if (!documents.contains (documentUri)) {
                            Object document = loadDocument (documentUri);
                            documents.add (documentUri, document);
                            collectReferences (baseUri, documentUri, document, references);
                        }

                        references.add (baseUri, documentUri, ref);
                    }
                }
            } else {
                bucket.walkPropertyTree (name, props -> {
                    collectReferences (baseUri, bucket.getSource (), props, references);
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

    private Object loadDocument (URI documentUri) throws ResolverException {
        try {
            return converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve %s.", documentUri), e);
        }
    }

    private Object loadDocument (String resourcePath) throws SchemaStoreException {
        try {
            return converter.convert (Strings.of (getClass ().getResourceAsStream (resourcePath)));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve %s.", resourcePath), e);
        }
    }
}
