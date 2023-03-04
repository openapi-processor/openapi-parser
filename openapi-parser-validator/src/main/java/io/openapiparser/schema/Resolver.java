/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.Converter;
import io.openapiparser.Reader;
import io.openapiparser.support.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.support.Nullness.nonNull;

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


    @Deprecated
    private final Reader reader;
    @Deprecated
    private final Converter converter;

    private final DocumentStore documents;
    private final DocumentLoader loader;
    private final Settings settings;

    public Resolver (DocumentStore documents, DocumentLoader loader, Settings settings) {
        this.documents = documents;
        this.loader = loader;
        this.settings = settings;
        this.reader = null;
        this.converter = null;
    }

    @Deprecated
    public Resolver (Reader reader, Converter converter, DocumentStore documents) {
        this.documents = documents;
        this.loader = null;
        this.settings = new Settings (SchemaVersion.Draft4);
        this.reader = reader;
        this.converter = converter;
    }

    @Deprecated
    public Resolver (Reader reader, Converter converter, DocumentStore documents, Settings settings) {
        this.documents = documents;
        this.loader = null;
        this.settings = settings;
        this.reader = reader;
        this.converter = converter;
    }

    public ResolverResult resolve (URI uri) {
        try {
            return resolve (uri, loader.loadDocument (uri));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", uri), e);
        }
    }

    @Deprecated
    public ResolverResult resolve (String resourcePath) {
        return resolve (URI.create (resourcePath), loadDocument (resourcePath));
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

        return new ResolverResult (currentScope, document, registry);
    }

    private void resolveIds (Scope currentScope, ReferenceRegistry registry) {
        Map<URI, Document> docs = documents.getDocuments ();
        docs.forEach ((documentUri, document) -> {
            Ref ref = new Ref (currentScope, documentUri.toString ());
            Scope scope = currentScope.move (documentUri, document.getDocument ());
            registry.add (ref, scope, document.getDocument ());
        });
    }

    private void collectReferences (URI documentUri, Object document, References registry)
        throws ResolverException {

        Scope scope = Scope.createScope (documentUri, document, settings.version);
        Bucket bucket = toBucket (scope, document);
        if (bucket == null)
            return;

        collectReferences (bucket, registry);
    }

    private void collectReferences (Scope currentScope, URI documentUri, Object document, References registry)
        throws ResolverException {

        Scope documentScope = Scope.createScope (documentUri, document, currentScope);
        Bucket bucket = toBucket (documentScope, document);
        if (bucket == null)
            return;
        // ids ????
        collectReferences (bucket, registry);
    }

    private void collectReferences (Bucket bucket, References references)
        throws ResolverException {

        Scope scope = bucket.getScope ();

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
                for (Object item : (Collection<?>) value) {
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

    // same file or not ???
    private void resolveReferences (References references, ReferenceRegistry registry) {
        references.each ((Ref ref) -> {
            // $ref == $id?
            URI id = ref.getAbsoluteUri ();
            Object idDocument = getDocument (id);
            if (idDocument != null) {
                // if ref references an id, it should not resolve the scope????
                Scope docScope = Scope.createScope (id, idDocument, ref.getScope ());
//                Scope docScope = getScope (ref.getScope (), idDocument);  // $id ???
//                Scope docScope2 = getScope (id, idDocument, settings.version);
                registry.add (ref, docScope, idDocument);
                return;
            }

            // no, try to resolve by document and pointer
            URI documentUri = ref.getDocumentUri ();
            Object document = getDocument (documentUri);
            Scope scope = Scope.createScope (documentUri, document, ref.getScope ());  // ref.getScope().move()
//            Scope scope = getScope (ref.getScope (), document); // if to id???
            //Scope scope2 = getScope (documentUri, document, settings.version);
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

    @Deprecated
    private @Nullable Bucket toBucket (URI uri, @Nullable Object source) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (uri, asMap (source));
    }

    private @PolyNull Bucket toBucket (Scope scope, @PolyNull Object source) {
        if (!isObject (source)) {
            return null;
        }
        return new Bucket (scope, asMap (source));
    }

    private @PolyNull Bucket toBucket (Scope scope, @Nullable Object source, JsonPointer location) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (scope, location, asMap (source));
    }

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
                    sourceScope.getId ()),
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

    private boolean isMap (Object value) {
        return value instanceof Map;
    }

    private boolean isString (Object value) {
        return value instanceof String;
    }

    private Ref createRef (URI scope, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in '%s'.", scope));
        }
        return new Ref (scope, ref);
    }

    private Ref createRef (Scope scope, String name, Object value) {
        String ref = convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in '%s'.", scope));
        }
        return new Ref (scope, ref);
    }

    private Object loadDocument (URI documentUri) throws ResolverException {
        try {
            return loader.loadDocument (documentUri);
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", documentUri), e);
        }
    }

    @Deprecated
    private Object loadDocument (String resourcePath) throws SchemaStoreException {
        try {
            InputStream source = nonNull (getClass ().getResourceAsStream (resourcePath));
            return converter.convert (Strings.of (source));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", resourcePath), e);
        }
    }
}
