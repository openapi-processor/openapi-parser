/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.BooleanConverter;
import io.openapiparser.converter.StringNullableConverter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;

import static io.openapiparser.converter.Types.*;

public class IdCollector {
    private final DocumentStore documents;

    public IdCollector (DocumentStore documents) {
        this.documents = documents;
    }

    public void walk (Bucket bucket) {
        addRoot (bucket);
        walkBucket (bucket);
    }

    private void addRoot (Bucket bucket) {
        Scope scope = bucket.getScope ();
        URI documentUri = scope.getDocumentUri ();
        URI baseUri = bucket.getBaseUri ();

        Object document = bucket.getRawValues ();

        if (!documents.contains (documentUri)) {
            documents.addId (documentUri, document);
        }

        if (!documents.contains (baseUri)) {
            documents.addId (baseUri, document);
        }
    }

    private void walkBucket (Bucket bucket) {
        Scope scope = bucket.getScope ();
        SchemaVersion version = scope.getVersion ();

        if (bucket.hasId ()) {
            registerId (scope.getBaseUri (), bucket);
        }

        // since 2019-09
        URI currentAnchor = getAnchor (bucket);
        registerAnchor (currentAnchor, bucket);

        // only 2019-09
        URI recursiveAnchor = getRecursiveAnchor (bucket);
        registerDynamicAnchor (recursiveAnchor, bucket);

        // since 2020-12
        URI dynamicAnchor = getDynamicAnchor (bucket);
        registerDynamicAnchor (dynamicAnchor, bucket);

        JsonPointer location = bucket.getLocation ();
        bucket.forEach ((name, value) -> {
            JsonPointer keywordLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            if (keyword == null || !keyword.isNavigatable ())
                return;

            if (keyword.isSchema ()) {
                walkSchema (keywordLocation, scope, value);

            } else if (keyword.isSchemaArray ()) {
                walkSchemaArray (keywordLocation, scope, value);

            } else if (keyword.isSchemaMap ()) {
                walkSchemaMap (keywordLocation, scope, value);
            }
        });
    }

    private void registerId (URI documentUri, Bucket bucket) {
        if (!documents.contains (documentUri)) {
            documents.addId (documentUri, bucket.getRawValues ());
        }
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

        if (!documents.contains (anchor)) {
            documents.addAnchor (anchor, bucket.getRawValues ());
        }
    }

    private @Nullable URI getRecursiveAnchor (Bucket bucket) {
        Boolean anchor = bucket.convert ("$recursiveAnchor", new BooleanConverter ());
        if (anchor == null)
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

        if (!documents.contains (anchor)) {
            documents.addDynamicAnchor (anchor, bucket.getRawValues ());
        }
    }

    private void walkSchema (JsonPointer location, Scope currentScope, Object value) {
        Scope scope = currentScope.move (value);
        Bucket bucket = toBucket (scope, location, value);
        if (bucket != null) {
            walkBucket (bucket);
        }
    }

    private void walkSchemaArray (JsonPointer location, Scope currentScope, Object value) {
        Collection<Object> keywordItems = toCollection (value);
        if (keywordItems != null) {
            int index = 0;
            for (Object item : keywordItems) {
                JsonPointer itemLocation = location.append (index);
                walkSchema (itemLocation, currentScope, item);
                index++;
            }
        }
    }

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

    private @Nullable Bucket toBucket(Scope scope, JsonPointer location, Object source) {
        if (!isObject (source)) {
            return null;
        }
        return new Bucket (scope, location, asMap (source));
    }

    private @Nullable Collection<Object> toCollection (Object value) {
        if (!isArray (value))
            return null;

        return asArray (value);
    }
}
