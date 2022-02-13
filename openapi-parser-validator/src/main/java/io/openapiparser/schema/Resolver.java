/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.Converter;
import io.openapiparser.Reader;
import io.openapiparser.support.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.*;

/**
 * loads the base document and resolves all internal and external $ref's. In case of an external
 * $ref it automatically downloads the referenced document.
 */
public class Resolver {
    private static final Logger log = LoggerFactory.getLogger (Resolver.class);

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
        documents.add (uri, document);  // todo add with id

        collectReferences (uri, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (uri, document, registry);
    }

    public ResolverResult resolve (String resourcePath) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        URI uri = URI.create (resourcePath);
        Object document = loadDocument (resourcePath);
        documents.add (uri, document); // todo add with id

        collectReferences (uri, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (uri, document, registry);
    }

    /**
     * resolves a given {@code document}. It will load any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param uri document uri
     * @param document document content
     * @return resolver result
     */
    public ResolverResult resolve (URI uri, Object document) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        URI scope = getScope (uri, document);
        documents.add (scope, document);

        collectReferences (scope, uri, document, registry);
        resolveReferences (registry);

        return new ResolverResult (scope, document, registry);
    }

    private URI getScope (URI uri, Object document) {
        URI scope = uri;

        if (!(document instanceof Map)) {
            return scope;
        }

        Map<String, Object> o = asMap (document);
        if (o.containsKey ("id")) {
            String id = as (o.get ("id"));
            if (id != null) {
                scope = URI.create (id);
            }
        }
        return scope;
    }

    private void collectReferences (
        URI baseUri, URI uri, Object document, ReferenceRegistry registry) throws ResolverException {

        Bucket bucket = toBucket (uri, document);
        if (bucket == null)
            return;

        collectReferences (baseUri, uri, bucket, registry);
    }

    private void collectReferences (
        URI baseUri, URI uri, Bucket bucket, ReferenceRegistry references)
        throws ResolverException {

        URI scope = collectScope (baseUri, bucket);

        bucket.forEach((name, value) -> {
            if (name.equals (Keywords.REF) && value instanceof String) {
                Ref ref = getRef (uri, name, value);

                URI documentUri = scope.resolve (ref.getDocumentUri (uri));

                if (!hasDocument (documentUri)) {
                    Object document = addDocument (uri, documentUri);
                    if (document != null) {
                        collectReferences (scope, documentUri, document, references);
                    }
                }

                addReference (scope, documentUri, ref, references);

            } else {
                bucket.walkPropertyTree (name, props -> {
                    collectReferences (scope, bucket.getSource (), props, references);
                });
            }
        });
    }

    private URI collectScope (URI baseUri, Bucket bucket) {
        URI scope = baseUri;

        if (!bucket.hasProperty ("id")) {
            return scope;
        }

        String id = as (bucket.getRawValue ("id"));
        if (id == null) {
            throw new RuntimeException (); // todo
        }

        scope = baseUri.resolve (id);
        if (!documents.contains (scope)) {
            documents.add (scope, bucket.getRawValues ());
        }
        return scope;
    }

    private void resolveReferences (ReferenceRegistry references) {
        references.resolve(this::resolve);
    }

    private Object resolve (URI documentUri, String documentRef) {
        Object document = getDocument (documentUri);
        Bucket bucket = toBucket (documentUri, document);
        if (bucket == null)
            return document;

        Ref ref = new Ref (documentRef);

        if (!ref.hasPointer ()) {
            return bucket.getRawValues ();
        }

        Object property = bucket.getRawValue (JsonPointer.from (ref.getPointer ()));
        if (property == null) {
            throw new ResolverException (String.format ("failed to resolve ref %s/%s.", documentUri, ref));
        }
        return property;
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
            documents.add (documentUri);
            log.info (String.format ("failed to resolve %s/$ref", uri), ex);
//            throw new ResolverException (String.format ("failed to resolve %s/$ref", uri), ex);
            return null;
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
