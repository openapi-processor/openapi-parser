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

    private void collectReferences (
        URI base, URI uri, Object document, ReferenceRegistry registry) throws ResolverException {

        Bucket bucket = toBucket (uri, document);
        if (bucket == null)
            return;

        collectReferences (base, uri, bucket, registry);
    }

    private void collectReferences (
        URI baseUri, URI uri, Bucket bucket, ReferenceRegistry references)
        throws ResolverException {

        bucket.forEach((name, value) -> {
            if (name.equals (Keywords.REF)) {
                Ref ref = getRef (uri, name, value);

                URI documentUri = ref.getDocumentUri (uri);

                if (!hasDocument (documentUri)) {
                    Object document = addDocument (uri, documentUri);
                    collectReferences (baseUri, documentUri, document, references);
                }

                addReference (baseUri, documentUri, ref, references);

            } else {
                bucket.walkPropertyTree (name, props -> {
                    collectReferences (baseUri, bucket.getSource (), props, references);
                });
            }
        });
    }

    private void resolveReferences (ReferenceRegistry references) {
        references.resolve((documentUri, documentRef) -> {
            Object document = getDocument (documentUri);
            Bucket bucket = toBucket (documentUri, document);
            if (bucket == null)
                return document;

            Ref ref = new Ref (documentRef);
            if (ref.hasPointer ()) {
                Object property = bucket.getRawValue (ref.getPointer ());
                if (property == null) {
                    throw new ResolverException (
                        String.format ("failed to resolve ref %s/%s.", documentUri, ref));
                }
                return property;
            } else {
                return bucket.getRawValues ();
            }
        });
    }

    private @Nullable Bucket toBucket(URI uri, Object source) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (uri, asMap (source));
    }

    private void addReference (
        URI baseUri, URI documentUri, Ref ref, ReferenceRegistry references) {

        references.add (baseUri, documentUri, ref.toString ());
    }

    private Object getDocument (URI documentUri) {
        return documents.get (documentUri);
    }

    private Object addDocument (URI uri, URI documentUri) {
        try {
            Object document = loadDocument (documentUri);
            documents.add (documentUri, document);
            return document;
        } catch (ResolverException ex) {
            throw new ResolverException (String.format ("failed to resolve %s/$ref", uri), ex);
        }
    }

    private boolean hasDocument (URI documentUri) {
        return documents.contains (documentUri);
    }

    private Ref getRef (URI uri, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in %s.", uri));
        }
        return new Ref(ref);
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
