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

import static io.openapiprocessor.jsonschema.support.Null.nonNull;
import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

public class ResolverId {

    private final ResolverContext context;
    private final SchemaDetector detector;

    public ResolverId (ResolverContext context) {
        this.context = context;
        this.detector = new JsonSchemaDetector();
    }

    public ResolverId (ResolverContext context, SchemaDetector detector) {
        this.context = context;
        this.detector = detector;
    }

    public void resolve (Bucket bucket) {
        resolveId (bucket);
        walkBucket (bucket);
    }

    @SuppressWarnings({"dereference.of.nullable"})
    private void walkBucket (Bucket bucket) {
        JsonPointer location = bucket.getLocation ();

        if (detector.isJsonSchema(location)) {
            // move this to the detector? by version?
            URI currentId = getId (bucket);
            registerId (currentId, bucket);
            resolveId (bucket);

            // since 2019-09
            URI currentAnchor = getAnchor (bucket);
            registerAnchor (currentAnchor, bucket);
            resolveAnchor (currentAnchor, bucket);

            // only 2019-09
            URI recursiveAnchor = getRecursiveAnchor (bucket);
            registerDynamicAnchor (recursiveAnchor, bucket);
            resolveDynamicAnchor (recursiveAnchor, bucket);

            // since 2020-12
            URI dynamicAnchor = getDynamicAnchor (bucket);
            registerDynamicAnchor (dynamicAnchor, bucket);
            resolveDynamicAnchor (dynamicAnchor, bucket);
        }

        Scope scope = bucket.getScope ();

        bucket.forEach ((name, value) -> {
            JsonPointer propLocation = location.append (name);

            if (detector.shouldWalkObject(scope, value, propLocation)) {
                walkSchema (scope, nonNull(value), propLocation);

            } else if (detector.shouldWalkArray(scope, value, propLocation)) {
                walkSchemaArray (scope, nonNull(value), propLocation);

            } else if (detector.shouldWalkMap(scope, value, propLocation)) {
                walkSchemaMap (scope, nonNull(value), propLocation);
            }
        });
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
            walkSchema (targetScope, requiresNonNull(propValue), propLocation);
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
