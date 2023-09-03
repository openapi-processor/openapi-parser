/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;

public class ResolverRef {
    private final ResolverContext context;

    public ResolverRef (ResolverContext context) {
        this.context = context;
    }


    public void resolve (Bucket bucket) {
        context.addDocument(bucket.getScope(), bucket.getRawValues());
        walkBucket (bucket);
        resolve ();
    }

    @SuppressWarnings({"dereference.of.nullable"})
    private void walkBucket (Bucket bucket) {
        Scope scope = bucket.getScope ();
        JsonPointer location = bucket.getLocation ();
        SchemaVersion version = scope.getVersion ();

        bucket.forEach ((name, value) -> {
            JsonPointer propLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            boolean navigable = keyword != null && keyword.isNavigable ();

            if (name.equals (Keywords.SCHEMA) && Types.isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, propLocation);

            } else if (name.equals (Keywords.REF) && Types.isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, propLocation);

            } else if (name.equals (Keywords.DYNAMIC_REF) && Types.isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, propLocation);

            } else if (name.equals (Keywords.RECURSIVE_REF) && Types.isString (value)) {
                Ref ref = createRef (scope, name, value);
                walkRef (ref, propLocation);

            } else if (navigable && keyword.isSchema () && Types.isObject (value)) {
                    walkSchema (scope, value, propLocation);

            } else if (navigable && keyword.isSchemaArray () && Types.isArray (value)) {
                walkSchemaArray (scope, value, propLocation);

            } else if (navigable && keyword.isSchemaMap ()) {
                walkSchemaMap (scope, value, propLocation);

            } else if (name.equals (Keywords.DEFAULT) && Types.isObject (value)) {
                walkSchema (scope, value, propLocation);

            } else if (keyword == null && Types.isObject (value)) {
                walkSchema (scope, value, propLocation);

            } else if (keyword == null && Types.isArray (value)) {
                walkSchemaArray (scope, value, propLocation);
            }
        });
    }

    private Ref createRef (Scope scope, String name, Object value) {
        String ref = Types.convertOrNull (name, value, String.class);
        if (ref == null) {
            throw new ResolverException (String.format ("failed to resolve empty $ref in '%s'.", scope));
        }
        return new Ref (scope, ref);
    }

    private void walkRef (Ref ref, JsonPointer location) {
        Scope scope = ref.getScope ();
        URI uri = ref.getDocumentUri ();

        Object document = context.getDocument (uri);
        if (document == null) {
            // todo no auto load -> throw
            document = addDocument (scope, uri, ref);
        }

        if (!context.isProcessedDocument (uri)) {
            context.setProcessedDocument (uri);

            Scope docScope = scope.move (uri, document);  // todo document != null
            Bucket bucket = Bucket.createBucket(docScope, document, JsonPointer.EMPTY);
            if (bucket == null) {
                return; // todo error
            }

            walkIds (bucket);
            walkBucket (bucket);
        }

        addReference (ref);
    }

    private void addReference (Ref ref) {
        context.addRef (ref);
    }

    private void walkIds (Bucket bucket) {
        ResolverId resolverId = new ResolverId (context);
        resolverId.resolve(bucket);
    }

    private void walkSchema (Scope currentScope, Object value, JsonPointer location) {
        Scope scope = currentScope.move (value);
        Bucket bucket = Bucket.createBucket(scope, value, location);
        if (bucket == null) {
            return; // todo error
        }

        walkBucket (bucket);
    }

    private void walkSchemaArray (Scope currentScope, Object value, JsonPointer location) {
        Collection<Object> items = Types.asArray (value);
        if (items == null) {
            return; // todo error
        }

        int index = 0;
        for (Object item : items) {
            JsonPointer itemLocation = location.append (index);
            walkSchema (currentScope, item, itemLocation);
            index++;
        }
    }

    private void walkSchemaMap (Scope currentScope, Object value, JsonPointer location) {
        Scope targetScope = currentScope.move (value);
        Bucket bucket = Bucket.createBucket(targetScope, value, location);
        if (bucket == null) {
            return; // // todo error
        }

        bucket.forEach ((propName, propValue) -> {
            JsonPointer propLocation = location.append (propName);
            walkSchema (targetScope, propValue, propLocation);
        });
    }

    private void resolve () {
        context.references.each ((Ref ref) -> {
            // $ref == $id?
            URI id = ref.getAbsoluteUri ();
            Object idDocument = context.getDocument (id);
            if (idDocument != null) {
                Scope docScope = ref.getScope ().move (id, idDocument);
                context.addRef (ref, docScope, idDocument);
                return;
            }

            // no, try to resolve by document and pointer
            URI documentUri = ref.getDocumentUri ();
            Object document = context.getDocument (documentUri);
            // todo check null
            Scope scope = ref.getScope ().move (documentUri, document);
            Bucket bucket = Bucket.createBucket(scope, document);

            // no object -> to (simple) value
            if (bucket == null) {
                assert document != null;
                context.addRef (ref, scope, document);
                return;
            }

            // object, no pointer -> to document
            if (!ref.hasPointer ()) {
                context.addRef (ref, scope, document);
                return;
            }

            RawValue referenced = bucket.getRawValue (JsonPointer.from (ref.getPointer ()));
            if (referenced == null) {
                throw new ResolverException (String.format ("failed to resolve ref <%s/%s>.", documentUri, ref));
            }

            context.addRef (ref, referenced.getScope (), referenced.getValue ()); // todo value is null ?
        });
    }

    private @Nullable Object addDocument (Scope scope, URI uri, Ref ref) {
        if (!ref.hasDocument ())
            return null;

        return context.addDocument (uri, scope.getDocumentUri ().toString (), ref.getRef ());
    }
}
