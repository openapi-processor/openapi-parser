/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.schema.Scope.createScope;

/**
 * loads the base document and resolves all internal and external $ref's. In case of an external
 * $ref it automatically downloads the referenced document.
 * <p>
 * todo make automatic download optional
 * todo prefill documents???
 * todo try to remove version
 * todo just use DocumentStore internally ????
 */
public class Resolver {
    private static final Logger log = LoggerFactory.getLogger (Resolver.class);

    public static class Settings {
        private final SchemaVersion version;
        private boolean loadSchemaFromUri = false;

        public Settings (SchemaVersion version) {
            this.version = version;
        }

        public Settings loadSchemasFromUri(boolean load) {
            this.loadSchemaFromUri = load;
            return this;
        }

        public SchemaVersion getVersion () {
            return version;
        }
    }

    private final DocumentStore documents;
    private final DocumentLoader loader;
    private final Settings settings;

    public Resolver (DocumentStore documents, DocumentLoader loader) {
        this.documents = documents;
        this.loader = loader;
        this.settings = new Settings (SchemaVersion.getLatest ());
    }

    public Resolver (DocumentStore documents, DocumentLoader loader, Settings settings) {
        this.documents = documents;
        this.loader = loader;
        this.settings = settings;
    }

    public ResolverResult resolve (URI uri) {
        try {
            return resolve (uri, loader.loadDocument (uri));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", uri), e);
        }
    }

    @Deprecated // ?
    public ResolverResult resolve (String resourcePath) {
        try {
            return resolve (URI.create (resourcePath), loader.loadDocument (resourcePath));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", resourcePath), e);
        }
    }


    /**
     * resolves a given {@code document}. It will walk any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param documentUri document uri
     * @param document document content
     * @return resolver result
     *
     * todo try/catch
     */
    public ResolverResult resolve (URI documentUri, Object document) {
        /*
        ReferenceRegistry registry = new ReferenceRegistry ();

        Scope currentScope = Scope.createScope (documentUri, document, settings.version);
        if (!isObject (document)) {
            return new ResolverResult (currentScope, document, registry);
        }

        Bucket bucket = toBucket (currentScope, document);

        IdCollector idCollector = new IdCollector (documents);
        idCollector.walk (bucket);
        resolveIds (currentScope, registry);

        References pending = new References ();
        collectReferences (bucket, pending);
        resolveReferences (pending, registry);
    */

        ReferenceRegistry registry = new ReferenceRegistry ();

        Scope scope = createScope (documentUri, document, settings.version);
        Bucket bucket = toBucket (scope, document);

        if (bucket == null) {
            return new ResolverResult (scope, document, registry);
        }

        //References pending = new References ();
        ResolverContext context = new ResolverContext (documents, loader, registry);

        ResolverId resolverId = new ResolverId (context);
        resolverId.resolve(bucket);

        ResolverRef resolverRef = new ResolverRef (context);
        resolverRef.resolve(bucket);

//        walkBucketIds (bucket, context); // phase one
//        walkBucket (bucket, context);    // phase two

//        collectReferences (scope, documentUri, document, context);
//        resolveReferences (pending, registry); // phase three

        return new ResolverResult (scope, document, registry);
    }



    private void walkBucket (Bucket bucket, ResolverContext context) {
//        URI currentId = getId (bucket);
//        registerId (currentId, bucket);
//        resolve (currentId, bucket, context);

        // since 2019-09
//        URI currentAnchor = getAnchor (bucket);
//        registerAnchor (currentAnchor, bucket);
//        resolve(currentAnchor, bucket, context);

        // only 2019-09
//        URI recursiveAnchor = getRecursiveAnchor (bucket);
//        registerDynamicAnchor (recursiveAnchor, bucket);
//        resolve(recursiveAnchor, bucket, context);

        // since 2020-12
//        URI dynamicAnchor = getDynamicAnchor (bucket);
//        registerDynamicAnchor (dynamicAnchor, bucket);
//        resolve(dynamicAnchor, bucket, context);


        Scope scope = bucket.getScope ();
        JsonPointer location = bucket.getLocation ();
        SchemaVersion version = scope.getVersion ();

        bucket.forEach ((name, value) -> {
            JsonPointer keywordLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            // check vocabulary?

            if (name.equals (Keywords.SCHEMA) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, keywordLocation, context);

            } else if (name.equals (Keywords.REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, keywordLocation, context);

            } else if (name.equals (Keywords.RECURSIVE_REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, keywordLocation, context);
            }

            if (keyword == null || !keyword.isNavigable ())
                return;

            if (keyword.isSchema () && isObject (value)) {
                walkSchema (scope, value, keywordLocation, context);

            } else if (keyword.isSchemaArray ()) {
                walkSchemaArray (scope, value, keywordLocation, context);

            } else if (keyword.isSchemaMap ()) {
                walkSchemaMap (scope, value, keywordLocation, context);
            }
        });
    }

    private void walkRef (Ref ref, JsonPointer location, ResolverContext context) {
//        Scope scope = ref.getScope ();
//        URI uri = ref.getDocumentUri ();
//
//        Object document = getDocument (uri);
//        if (document == null) {
//            // todo no auto load -> throw
//            document = addDocument (scope, uri, ref);
//            if (document != null && !context.isProcessedDocument (uri)) {
//                context.setProcessedDocument (uri);
//                walkSchema (scope, document, location, context);
//            }
//        }
//
//        if (context.isProcessedReference(ref.getAbsoluteUri ())) {
//            return;
//        }
//
//        context.setProcessedReferences(ref.getAbsoluteUri ());
//        addReference (ref, context.references);
    }

    private void walkSchema (
        Scope currentScope, Object value, JsonPointer location, ResolverContext context
    ) {
        Scope scope = currentScope.move (value);
        Bucket bucket = toBucket (scope, value, location);
        if (bucket == null) {
            return; // todo error
        }

        walkBucket (bucket, context);
    }

    private void walkSchemaArray (
        Scope currentScope, Object value, JsonPointer location, ResolverContext context
    ) {
        Collection<Object> items = asArray (value);
        if (items == null) {
            return; // todo error
        }

        int index = 0;
        for (Object item : items) {
            JsonPointer itemLocation = location.append (index);
            walkSchema (currentScope, item, itemLocation, context);
            index++;
        }
    }

    private void walkSchemaMap (
        Scope currentScope, Object value, JsonPointer location, ResolverContext context
    ) {
        Scope targetScope = currentScope.move (value);
        Bucket bucket = toBucket (targetScope, value, location);
        if (bucket == null) {
            return; // // todo error
        }

        bucket.forEach ((propName, propValue) -> {
            JsonPointer propLocation = location.append (propName);
            walkSchema (targetScope, propValue, propLocation, context);
        });
    }

//    private void resolveIds (Scope currentScope, ReferenceRegistry registry) {
//        Map<URI, Document> docs = documents.getDocuments ();
//        docs.forEach ((documentUri, document) -> {
//            Ref ref = new Ref (currentScope, documentUri.toString ());
//            Scope scope = currentScope.move (documentUri, document.getDocument ());
//            registry.add (ref, scope, document.getDocument ());
//        });
//    }

    private void collectReferences (URI documentUri, Object document, References references)
        throws ResolverException {

        Scope scope = createScope (documentUri, document, settings.version);
        Bucket bucket = toBucket (scope, document);
        if (bucket == null)
            return;

        collectReferences (bucket, references);
    }

    // todo
    private void collectReferences (Scope currentScope, URI documentUri, Object document, References references)
        throws ResolverException {

        Scope documentScope = createScope (documentUri, document, currentScope);
        Bucket bucket = toBucket (documentScope, document);
        if (bucket == null)
            return;
        // ids ????
        collectReferences (bucket, references);
    }

    private void collectId (Bucket bucket, ResolverContext context) {
//        URI currentId = getId (bucket);
//        registerId (currentId, bucket);
//        resolve (currentId, bucket, context);
//
//        // since 2019-09
//        URI currentAnchor = getAnchor (bucket);
//        registerAnchor (currentAnchor, bucket);
//        resolve(currentAnchor, bucket, context);
//
//        // only 2019-09
//        URI recursiveAnchor = getRecursiveAnchor (bucket);
//        registerDynamicAnchor (recursiveAnchor, bucket);
//        resolve(recursiveAnchor, bucket, context);
//
//        // since 2020-12
//        URI dynamicAnchor = getDynamicAnchor (bucket);
//        registerDynamicAnchor (dynamicAnchor, bucket);
//        resolve(dynamicAnchor, bucket, context);
    }

    private void resolve (@Nullable URI uri, Bucket bucket, ResolverContext context) {
//        if (uri == null)
//            return;
//
//        if (context.registry.hasReference (uri)) {
//            return;
//        }
//
//        Scope scope = bucket.getScope ();
//        Ref ref = new Ref (scope, uri);
//        context.registry.add (ref, scope, bucket.getRawValues ());
//        context.setProcessedReferences (ref.getAbsoluteUri ()); // ????
    }

//    private @Nullable URI getId (Bucket bucket) {
//        String id = bucket.getScope ().getVersion ().getIdProvider ().getId (bucket.getRawValues ());
//        if (id == null)
//            return null;
//
//        return bucket.getScope ().resolve(id).getBaseUri ();
//    }

//    private void registerId (@Nullable URI id, Bucket bucket) {
//        if (id == null)
//            return;
//
//        if (!documents.contains (id)) {
//            documents.addId (id, bucket.getRawValues ());
//        }
//    }
//
//    private @Nullable URI getAnchor (Bucket bucket) {
//        String anchor = bucket.convert ("$anchor", new StringNullableConverter ());
//        if (anchor == null)
//            return null;
//
//        return bucket.getScope ().resolveAnchor (anchor);
//    }
//
//    private void registerAnchor (@Nullable URI anchor, Bucket bucket) {
//        if (anchor == null)
//            return;
//
//        if (!documents.contains (anchor)) {
//            documents.addAnchor (anchor, bucket.getRawValues ());
//        }
//    }
//
//    private @Nullable URI getRecursiveAnchor (Bucket bucket) {
//        Boolean anchor = bucket.convert ("$recursiveAnchor", new BooleanConverter ());
//        if (anchor == null)
//            return null;
//
//        return bucket.getScope ().resolveAnchor ("");
//    }
//
//    private @Nullable URI getDynamicAnchor (Bucket bucket) {
//        String anchor = bucket.convert ("$dynamicAnchor", new StringNullableConverter ());
//        if (anchor == null)
//            return null;
//
//        return bucket.getScope ().resolveAnchor (anchor);
//    }
//
//    private void registerDynamicAnchor (@Nullable URI anchor, Bucket bucket) {
//        if (anchor == null)
//            return;
//
//        if (!documents.contains (anchor)) {
//            documents.addDynamicAnchor (anchor, bucket.getRawValues ());
//        }
//    }

    private void collectReferences (Scope currentScope, URI documentUri, Object document, ResolverContext context)
        throws ResolverException {

        Scope documentScope = createScope (documentUri, document, currentScope);
        Bucket bucket = toBucket (documentScope, document);
        if (bucket == null)
            return;

        //context.setHasReferences(documentUri);
        collectReferences (bucket, context);
    }

    @Deprecated
    private void collectReferences (Bucket bucket, References references)
        throws ResolverException {

        Scope scope = bucket.getScope ();
        SchemaVersion version = scope.getVersion ();

        // DO NOT CALL!!!
        bucket.forEach ((name, value) -> {
            if (name.equals (Keywords.SCHEMA) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                if (!hasDocument (documentUri)) {
                    // todo no auto load -> throw
                    Object document = addDocument (scope, documentUri, ref);
                    if (document != null) {
                        collectReferences (scope, documentUri, document, references);
                    }
                }

                addReference (ref, references);

            } else if (name.equals (Keywords.REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                if (!hasDocument (documentUri)) {
                    Object document = addDocument (scope, documentUri, ref);
                    if (document != null) {
                        collectReferences (scope, documentUri, document, references);
                    }
                }

                addReference (ref, references);

            } else if (name.equals (Keywords.RECURSIVE_REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                if (!hasDocument (documentUri)) {
                    Object document = addDocument (scope.getBaseUri (), documentUri, ref);
                    if (document != null) {
                        collectReferences (documentUri, document, references);
                    }
                }

                addReference (ref, references);

            } else if (isObject (value)) {
                // adjust location to bucket scope?
                JsonPointer location = bucket.getLocation ().append (name);
                Scope objectScope = scope.move(value);
                Bucket objectBucket = toBucket (objectScope, value, location);
                collectReferences (objectBucket, references);
            } else if (isArray (value)) {
                // adjust location to bucket scope?
                JsonPointer location = bucket.getLocation ().append (name);

                int index = 0;
                for (Object item : asArray (value)) {
                    if (!isObject (item))
                        continue;

                    Scope itemScope = scope.move (item);
                    Bucket itemBucket = toBucket (itemScope, item, location.append (index));
                    collectReferences (itemBucket, references);

                    index++;
                }
            }
        });
    }

    private void collectReferences (Bucket bucket, ResolverContext context)
        throws ResolverException {

        collectId (bucket, context);

        Scope scope = bucket.getScope ();
        SchemaVersion version = scope.getVersion ();

        /*
                JsonPointer location = bucket.getLocation ();
        bucket.forEach ((name, value) -> {
            JsonPointer keywordLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            if (keyword == null || !keyword.isNavigable ())
                return;

            if (keyword.isSchema ()) {
                walkSchema (keywordLocation, scope, value);

            } else if (keyword.isSchemaArray ()) {
                walkSchemaArray (keywordLocation, scope, value);

            } else if (keyword.isSchemaMap ()) {
                walkSchemaMap (keywordLocation, scope, value);
            }
        });
         */


        bucket.forEach ((name, value) -> {
            Keyword keyword = version.getKeyword (name);
            if (keyword == null) {
                keyword = Keyword.NONE;
            }

            if (name.equals (Keywords.SCHEMA) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                Object document = getDocument (documentUri);
                if (document == null) {
                    // todo no auto load -> throw
                    document = addDocument (scope, documentUri, ref);
                }

//                if (context.hasReferences(documentUri)) {
//                    return;
//                }
//
//                context.setHasReferences(documentUri);
                collectReferences (scope, documentUri, document, context);
                addReference (ref, context.references);

            } else if (name.equals (Keywords.REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                Object document = getDocument (documentUri);
                if (document == null) {
                    // todo no auto load -> throw
                    document = addDocument (scope, documentUri, ref);
                }

//                if (context.hasReferences(documentUri)) {
//                    return;
//                }
//
//                context.setHasReferences(documentUri);
                collectReferences (scope, documentUri, document, context);
                addReference (ref, context.references);

            } else if (name.equals (Keywords.RECURSIVE_REF) && isString (value)) {
                Ref ref = createRef (scope, name, value);
                URI documentUri = ref.getDocumentUri ();

                Object document = getDocument (documentUri);
                if (document == null) {
                    // todo no auto load -> throw
                    document = addDocument (scope, documentUri, ref);
                }

//                if (context.hasReferences (documentUri)) {
//                    return;
//                }
//
//                context.setHasReferences (documentUri);
                collectReferences (scope, documentUri, document, context);
                addReference (ref, context.references);

            } else if (keyword.isSchema ()) {

            } else if (keyword.isSchemaArray ()) {

            } else if (keyword.isSchemaMap ()) {



                /*
                    private void walkSchemaMap (JsonPointer location, Scope currentScope, Object value) {
        Scope targetScope = currentScope.move (value);
        Bucket keywordBucket = toBucket (targetScope, location, value);
        if (keywordBucket != null) {
            keywordBucket.forEach ((propName, propValue) -> {
                JsonPointer propLocation = location.append (propName);
                walkSchema (propLocation, targetScope, propValue);
            });
        }
    }
                 */


            } else if (isObject (value)) {
                // adjust location to bucket scope?
                JsonPointer location = bucket.getLocation ().append (name);
                Scope objectScope = scope.move(value);
                Bucket objectBucket = toBucket (objectScope, value, location);
                collectReferences (objectBucket, context);

            } else if (isArray (value)) {
                // adjust location to bucket scope?
                JsonPointer location = bucket.getLocation ().append (name);

                int index = 0;
                for (Object item : asArray (value)) {
                    if (!isObject (item))
                        continue;

                    Scope itemScope = scope.move (item);
                    Bucket itemBucket = toBucket (itemScope, item, location.append (index));
                    collectReferences (itemBucket, context);

                    index++;
                }
            }
        });


        /*
                    if (keyword == null || !keyword.isNavigable ())
                return;

            if (keyword.isSchema ()) {
                walkSchema (keywordLocation, scope, value);

            } else if (keyword.isSchemaArray ()) {
                walkSchemaArray (keywordLocation, scope, value);

            } else if (keyword.isSchemaMap ()) {
                walkSchemaMap (keywordLocation, scope, value);
            }
         */
    }
    // same file or not ???
    private void resolveReferences (References references, ReferenceRegistry registry) {
        references.each ((Ref ref) -> {
            // $ref == $id?
            URI id = ref.getAbsoluteUri ();
            Object idDocument = getDocument (id);
            if (idDocument != null) {
                Scope docScope = ref.getScope ().move (id, idDocument);
                // if ref references an id, it should not resolve the scope????
//                Scope docScope = Scope.createScope (id, idDocument, ref.getScope ());
//                Scope docScope = getScope (ref.getScope (), idDocument);  // $id ???
//                Scope docScope2 = getScope (id, idDocument, settings.version);
                registry.add (ref, docScope, idDocument);
                return;
            }

            // no, try to resolve by document and pointer
            URI documentUri = ref.getDocumentUri ();
            Object document = getDocument (documentUri);
            Scope scope = ref.getScope ().move (documentUri, document);
            Bucket bucket = toBucket (scope, document);

            // no object -> to (simple) value
            if (bucket == null) {
                assert document != null;
                registry.add (ref, scope, document);
                return;
            }

            // object, no pointer -> to document
            if (!ref.hasPointer ()) {
                registry.add (ref, scope, document);
                return;
            }

            RawValue referenced = bucket.getRawValueX (JsonPointer.from (ref.getPointer ()));
            if (referenced == null) {
                throw new ResolverException (String.format ("failed to resolve ref <%s/%s>.", documentUri, ref));
            }

            registry.add (ref, referenced.getScope (), referenced.getValue ());

            // may reduce the bucket stuff by extracting getRawValue()
//            RawValue property = bucket.getRawValue (
//                JsonPointer.from (ref.getPointer ()),
//                settings.version.getIdProvider ());
//
//            if (property == null) {
//                throw new ResolverException (String.format ("failed to resolve ref <%s/%s>.", documentUri, ref));
//            }

//            return property;
        });
    }

    private @Nullable Bucket toBucket (Scope scope, @PolyNull Object source) {
        if (!isObject (source)) {
            return null;
        }
        return new Bucket (scope, asObject (source));
    }

    private @PolyNull Bucket toBucket (Scope scope, @Nullable Object source, JsonPointer location) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (scope, location, asMap (source));
    }

//    private @Nullable Bucket toBucket(Scope scope, JsonPointer location, Object source) {
//        if (!isObject (source)) {
//            return null;
//        }
//        return new Bucket (scope, location, asMap (source));
//    }

    private void addReference (Ref ref, References references) {
        references.add (ref);
    }

    // move/rename scope parameter, oly used for error logging
    private @Nullable Object addDocument (URI sourceScopeId, URI documentUri, Ref ref) {
        try {
            if (!ref.hasDocument ())
                return null;

            Object document;
            if (documentUri.getScheme () == null) {
                document = loader.loadDocument (documentUri.toString ());
            } else {
                document = loader.loadDocument (documentUri);
            }

            documents.addId (documentUri, document);
            return document;
        } catch (ResolverException ex) {
            throw new ResolverException (
                String.format (
                    "failed to resolve '%s' $referenced from '%s'",
                    ref.getRef (),
                    sourceScopeId),
                ex);
        }
    }

    private @Nullable Object addDocument (Scope sourceScope, URI documentUri, Ref ref) {
        try {
            if (!ref.hasDocument ())
                return null;

            Object document;
            if (documentUri.getScheme () == null) {
                document = loader.loadDocument (documentUri.toString ());
            } else {
                document = loader.loadDocument (documentUri);
            }

            documents.addId (documentUri, document);
            return document;
        } catch (ResolverException ex) {
            throw new ResolverException (
                String.format (
                    "failed to resolve '%s' $referenced from '%s'",
                    ref.getRef (),
                    sourceScope.getBaseUri ()),
                ex);
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

        return settings.version.getIdProvider ().getId (asMap (value));
    }

    /**
     * extract the scope from {@code bucket} if available.
     *
     * @param bucket a bucket
     * @return scope id or null
     */
    private @Nullable String getScopeId (Bucket bucket) {
        return getScopeId (bucket.getRawValues ());
    }

    private boolean hasDocument (URI documentUri) {
        return documents.contains (documentUri);
    }

    private @Nullable Object getDocument (URI documentUri) {
        return documents.get (documentUri);
    }

    @Deprecated
    private boolean isMap (Object value) {
        return value instanceof Map;
    }

    @Deprecated
    private boolean isString (Object value) {
        return value instanceof String;
    }

    private Ref createRef (Scope scope, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in '%s'.", scope));
        }
        return new Ref (scope, ref);
    }

//    private Object loadDocument (URI documentUri) throws ResolverException {
//        try {
//            return loader.loadDocument (documentUri);
//        } catch (Exception e) {
//            throw new ResolverException (String.format ("failed to resolve '%s'.", documentUri), e);
//        }
//    }

//    @Deprecated
//    private Object loadDocument (String resourcePath) throws SchemaStoreException {
//        try {
//            InputStream source = nonNull (getClass ().getResourceAsStream (resourcePath));
//            return converter.convert (Strings.of (source));
//        } catch (Exception e) {
//            throw new ResolverException (String.format ("failed to resolve '%s'.", resourcePath), e);
//        }
//    }
}
