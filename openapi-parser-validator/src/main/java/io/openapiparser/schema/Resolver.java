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
    private final IdProvider idProvider;

    public Resolver (Reader reader, Converter converter, DocumentStore documents) {
        this.reader = reader;
        this.converter = converter;
        this.documents = documents;
        this.idProvider = new IdProvider (); // todo inject
    }

    public ResolverResult resolve (URI uri) {
        return resolve (uri, loadDocument (uri));
    }

    public ResolverResult resolve (String resourcePath) {
        return resolve (URI.create (resourcePath), loadDocument (resourcePath));
    }

    /**
     * entry point
     *
     * resolves a given {@code document}. It will load any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param scope document uri
     * @param document document content
     * @return resolver result
     */
    public ResolverResult resolve (URI scope, Object document) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        URI currentScope = getScope (scope, document);
        documents.add (currentScope, document);

        collectIds (currentScope, document);
        collectReferences (currentScope, document, registry);
        resolveReferences (registry);

        return new ResolverResult (currentScope, document, registry);
    }

    /**
     * calculates the scope of the {@code document}. If {@code document} contains a scope it is the
     * new scope otherwise it keeps the current {@code scope}.
     *
     * @param scope source scope
     * @param document source document
     * @return the scope of the document, may be same as {@code scope}
     */
    private URI getScope (URI scope, Object document) {
//        if (!(document instanceof Map)) {
//            return scope;
//        }

        String id = getScopeId (document);
        if (id != null) {
            return URI.create (id);
        }

        return scope;
    }

    // todo: getScope??? used
    private URI findScope (URI scope, Bucket bucket) {
        String id = getScopeId (bucket);
        if (id == null)
            return scope;

        Ref ref = new Ref (scope, id);
        URI scopeDocument = ref.getFullRefUri ();
        if (!hasDocument (scopeDocument)) {
            documents.add (scopeDocument, bucket.getRawValues ());
        }

        return scopeDocument;
//        return ref.getFullRefUri ();
    }

    private void collectIds (URI scope, Object document) {
        Bucket bucket = toBucket (scope, document);
        if (bucket == null)
            return;

        collectIds (scope, bucket);
    }

    private void collectIds (URI scope, Bucket bucket) {
        URI currentScope = findScope (scope, bucket);

        bucket.forEach ((name, value) -> {
            bucket.walkPropertyTree (name, props -> {
                // enum contains values
                if (name.equals ("enum"))
                    return;

                collectIds (currentScope, props);
            });
        });
    }

    private void collectReferences (URI scope, Object document, ReferenceRegistry registry)
        throws ResolverException {

        Bucket bucket = toBucket (scope, document);
        if (bucket == null)
            return;

        collectReferences (scope, bucket, registry);
    }

    private void collectReferences (URI scope, Bucket bucket, ReferenceRegistry references)
        throws ResolverException {

        URI scopeX = findScope (scope, bucket);

        bucket.forEach((name, value) -> {
            if (name.equals (Keywords.REF) && value instanceof String) {
                Ref ref = createRef (scopeX, name, value);
                URI documentUri = ref.getDocumentUri ();

                if (!hasDocument (documentUri)) {
                    Object document = addDocument (scopeX /*just error reporting*/, documentUri, ref);
                    if (document != null) {
                        collectReferences (documentUri/*scopeX*/,/*, documentUri,*/ document, references);
                    }
                }

                addReference (scopeX, documentUri, ref, references);

            } else {
                bucket.walkPropertyTree (name, props -> {
                    collectReferences (scopeX, /*, bucket.getSource (),*/ props, references);
                });
            }
        });
    }

    private void resolveReferences (ReferenceRegistry references) {
        references.resolveX(this::resolveX);
    }

    private Object resolve (/*URI documentUri, String documentRef*/ Ref ref) {
        // $ref points directly to id?
        URI id = ref.getFullRefUri ();
        Object idDocument = getDocument (id);
        if (idDocument != null)
            return idDocument;

        // no, try to resolve by document and pointer
        URI documentUri = ref.getDocumentUri ();
        Object document = getDocument (documentUri);
        Bucket bucket = toBucket (documentUri, document);
        if (bucket == null)
            return document;

        if (!ref.hasPointer ()) {
            return bucket.getRawValues ();
        }

        Object property = bucket.getRawValue (JsonPointer.from (ref.getPointer ()));
        if (property == null) {
            throw new ResolverException (String.format ("failed to resolve ref <%s/%s>.", documentUri, ref));
        }
        return property;
    }

    private RawValue resolveX (/*URI documentUri, String documentRef*/ Ref ref) {
        // $ref points directly to id?
        URI id = ref.getFullRefUri ();
        Object idDocument = getDocument (id);
        if (idDocument != null)
            return new RawValue (id, idDocument);

        // no, try to resolve by document and pointer
        URI documentUri = ref.getDocumentUri ();
        Object document = getDocument (documentUri);
        Bucket bucket = toBucket (documentUri, document);
        if (bucket == null)
            return new RawValue (documentUri, document);

        if (!ref.hasPointer ()) {
            return new RawValue(documentUri, bucket.getRawValues ());
        }

        RawValue property = bucket.getRawValueX (JsonPointer.from (ref.getPointer ()));
        if (property == null) {
            throw new ResolverException (String.format ("failed to resolve ref <%s/%s>.", documentUri, ref));
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

        references.add (ref);
//        references.add (baseUri, /*documentUri*/ref.getDocumentUri (), ref.getRef ());
    }

    private Object addDocument (URI uri /*????*/, URI documentUri, Ref ref) {
        try {
            if (!ref.hasDocument ())
                return null;

            Object document = loadDocument (documentUri);
            documents.add (documentUri, document);
            return document;
        } catch (ResolverException ex) {
//            documents.add (documentUri);
//            return null;
//            log.info (String.format ("failed to resolve %s/$ref", uri), ex);
            throw new ResolverException (String.format ("failed to resolve %s/$ref", uri), ex);
//            return null;
        }
    }

    /**
     * extract the scope from {@code value} if available.
     *
     * @param value a value
     * @return scope id or null
     */
    private @Nullable String getScopeId (Object value) {
        if (!isMap (value))
            return null;

        return idProvider.getId (asMap (value));

//        Object id = object.get ("id");
//
//        if (!isString (id))
//            return null;
//
//        return as (id);
    }

    /**
     * extract the scope from {@code bucket} if available.
     *
     * @param bucket a bucket
     * @return scope id or null
     */
    private @Nullable String getScopeId (Bucket bucket) {
//        Object ref = bucket.getRawValue ("$ref");
//        Object id = bucket.getRawValue ("id");
//        if (ref != null || !isString (id))
//            return null;
//
//        return as (id);

        return getScopeId (bucket.getRawValues ());
    }

    private boolean hasDocument (URI documentUri) {
        return documents.contains (documentUri);
    }

    private Object getDocument (URI documentUri) {
        return documents.get (documentUri);
    }

    private boolean isMap (Object value) {
        return value instanceof Map;
    }

    private boolean isString (Object value) {
        return value instanceof String;
    }

    private Ref createRef (URI scope, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in %s.", scope));
        }
        return new Ref(scope, ref);
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
