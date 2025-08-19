/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.BooleanConverter;
import io.openapiprocessor.jsonschema.converter.StringNullableConverter;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

public class ResolverId {

    private final ResolverContext context;

    interface SchemaDetector {
        boolean isSchema();
        boolean isSchemaArray();
        boolean isSchemaMap();
    }

    static class JsonSchemaDetector implements SchemaDetector {
        private final @Nullable Keyword keyword;

        public JsonSchemaDetector(SchemaVersion version, String propertyName) {
            this.keyword = version.getKeyword(propertyName);
        }

        @Override
        public boolean isSchema() {
            return keyword != null && keyword.isNavigable() && keyword.isSchema();
        }

        @Override
        public boolean isSchemaArray() {
            return keyword != null && keyword.isNavigable() && keyword.isSchemaArray();
        }

        @Override
        public boolean isSchemaMap() {
            return keyword != null && keyword.isNavigable() && keyword.isSchemaMap();
        }
    }

    public ResolverId (ResolverContext context) {
        this.context = context;
    }

    public void resolve (Bucket bucket) {
        resolveId (bucket);
        walkBucket (bucket, true);
    }

    @SuppressWarnings({"dereference.of.nullable"})
    private void walkBucket (Bucket bucket, boolean detectId) {
        if (detectId) {
            URI currentId = getId(bucket);
            registerId(currentId, bucket);
            resolveId(bucket);

            // since 2019-09
            URI currentAnchor = getAnchor(bucket);
            registerAnchor(currentAnchor, bucket);
            resolveAnchor(currentAnchor, bucket);

            // only 2019-09
            URI recursiveAnchor = getRecursiveAnchor(bucket);
            registerDynamicAnchor(recursiveAnchor, bucket);
            resolveDynamicAnchor(recursiveAnchor, bucket);

            // since 2020-12
            URI dynamicAnchor = getDynamicAnchor(bucket);
            registerDynamicAnchor(dynamicAnchor, bucket);
            resolveDynamicAnchor(dynamicAnchor, bucket);
        }

        Scope scope = bucket.getScope ();
        JsonPointer location = bucket.getLocation ();
        SchemaVersion version = scope.getVersion ();

        bucket.forEach ((name, value) -> {
            JsonPointer propLocation = location.append (name);

            // we only want to detect $id's in schema objects
            JsonSchemaDetector schemaDetector = new JsonSchemaDetector(version, name);

            if (schemaDetector.isSchema () && Types.isObject(value)) {
                walkObject(scope, value, propLocation, true);

            } else if (schemaDetector.isSchemaArray () && Types.isArray(value)) {
                walkArray(scope, value, propLocation, true);

            } else if (schemaDetector.isSchemaMap() && Types.isObject(value)) {
                walkMap(scope, value, propLocation, true);

            } else if (Types.isObject (value)) {
                walkObject(scope, value, propLocation, false);

            } else if (Types.isArray (value)) {
                walkArray(scope, value, propLocation, false);
            }
        });
    }

    private void walkObject (Scope currentScope, Object value, JsonPointer location, boolean detectId) {
        Scope scope = currentScope.move (value);
        Bucket bucket = Bucket.createBucket(scope, value, location);
        if (bucket == null) {
            return; // todo error
        }

        walkBucket (bucket, detectId);
    }

    private void walkArray(Scope currentScope, Object value, JsonPointer location, boolean detectId) {
        Collection<Object> items = Types.asArray(value);
        if (items == null) {
            return; // todo error
        }

        int index = 0;
        for (Object item : items) {
            JsonPointer itemLocation = location.append(index);
            walkObject(currentScope, item, itemLocation, detectId);
            index++;
        }
    }

    private void walkMap (Scope currentScope, @Nullable Object value, JsonPointer location, boolean detectId) {
        Scope targetScope = currentScope.move (requiresNonNull(value));
        Bucket bucket = Bucket.createBucket(targetScope, value, location);
        if (bucket == null) {
            return; // // todo error
        }

        bucket.forEach ((propName, propValue) -> {
            JsonPointer propLocation = location.append (propName);
            walkObject (targetScope, requiresNonNull(propValue), propLocation, detectId);
        });
    }

    private @Nullable URI getId (Bucket bucket) {
        return bucket.getId ();
    }

    private void registerId (@Nullable URI id, Bucket bucket) {
        if (id == null)
            return;

        // id
        context.addId (bucket.getBaseUri (), bucket.getRawValues ());
    }

    private @Nullable URI getAnchor (Bucket bucket) {
        String anchor = bucket.convert ("$anchor", new StringNullableConverter ());
        if (anchor == null)
            return null;

        return bucket.getScope ().resolveAnchor (anchor);
    }

    private void registerAnchor (@Nullable URI anchor, Bucket bucket) {
        if (anchor == null)
            return;

        context.addAnchor (anchor, bucket.getRawValues ());
    }

    private @Nullable URI getRecursiveAnchor (Bucket bucket) {
        Boolean anchor = bucket.convert ("$recursiveAnchor", new BooleanConverter ());
        if (anchor == null || !anchor)
            return null;

        return bucket.getScope ().resolveAnchor ("");
    }

    private @Nullable URI getDynamicAnchor (Bucket bucket) {
        String anchor = bucket.convert ("$dynamicAnchor", new StringNullableConverter ());
        if (anchor == null)
            return null;

        return bucket.getScope ().resolveAnchor (anchor);
    }

    private void registerDynamicAnchor (@Nullable URI anchor, Bucket bucket) {
        if (anchor == null)
            return;

        context.addDynamicAnchor (anchor, bucket.getRawValues ());
    }

    private void resolve (@Nullable URI uri, Bucket bucket) {
        if (uri == null)
            return;

        context.addRef (new Ref (bucket.getScope (), uri), bucket.getRawValues ());
    }

    private void resolveAnchor (@Nullable URI uri, Bucket bucket) {
        if (uri == null)
            return;

        context.addRef (new Ref (bucket.getScope (), uri), bucket.getRawValues ());
    }

    private void resolveDynamicAnchor (@Nullable URI uri, Bucket bucket) {
        if (uri == null)
            return;

        context.addDynamicAnchorRef (new Ref (bucket.getScope (), uri), bucket.getRawValues ());
    }

    private void resolveId (Bucket bucket) {
        context.addRef (new Ref (bucket.getScope (), bucket.getBaseUri ()), bucket.getRawValues ());
    }
}
