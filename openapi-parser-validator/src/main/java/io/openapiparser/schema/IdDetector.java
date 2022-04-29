/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.asMap;

/**
 * walks the object tree of the given {@code Bucket}, skipping "unknown" properties and inserts the
 * collected scopes (i.e. $ids) into the document store.
 */
public class IdDetector {
    private final SchemaVersion version;
    private final DocumentStore documents;

    public IdDetector (SchemaVersion version, DocumentStore documents) {
        this.version = version;
        this.documents = documents;
    }

    public void walk (URI scope, Bucket bucket) {
        JsonPointer location = bucket.getLocation ();
        URI currentScope = getScope (scope, bucket);
        registerScope (currentScope, bucket);

        bucket.forEach ((name, value) -> {
            JsonPointer keywordLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            if (keyword == null || !keyword.isNavigatable ())
                return;

            if (keyword.isSchema ()) {
                walkSchema (location, currentScope, value);

            } else if (keyword.isSchemaArray ()) {
                walkSchemaArray (keywordLocation, currentScope, value);

            } else if (keyword.isSchemaMap ()) {
                walkSchemaMap (keywordLocation, currentScope, value);
            }
        });
    }

    private void walkSchema (JsonPointer location, URI currentScope, Object value) {
        Bucket keywordBucket = toBucket (currentScope, location, value);
        if (keywordBucket != null) {
            walk (currentScope, keywordBucket);
        }
    }

    private void walkSchemaArray (JsonPointer location, URI currentScope, Object value) {
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

    private void walkSchemaMap (JsonPointer location, URI currentScope, Object value) {
        Bucket keywordBucket = toBucket (currentScope, location, value);
        if (keywordBucket != null) {
            keywordBucket.forEach ((propName, propValue) -> {
                JsonPointer propLocation = location.append (propName);
                walkSchema (propLocation, currentScope, propValue);
            });
        }
    }

    private URI getScope (URI scope, Bucket bucket) {
        String id = getScopeId (bucket);
        if (id == null)
            return scope;

        Ref ref = new Ref (scope, id);
        return ref.getFullRefUri ();
    }

    private void registerScope (URI scope, Bucket bucket) {
        if (!documents.contains (scope)) {
            documents.add (scope, bucket.getRawValues ());
        }
    }

    /**
     * extract the scope from {@code bucket} if available.
     *
     * @param bucket a bucket
     * @return scope id or null
     */
    private @Nullable String getScopeId (Bucket bucket) {
        return version.getIdProvider ().getId (bucket.getRawValues ());
    }

    @SuppressWarnings ("unchecked")
    private @Nullable Collection<Object> toCollection (Object value) {
        if (!(value instanceof Collection))
            return null;

        return (Collection<Object>) value;
    }

    private @Nullable Bucket toBucket(URI uri, JsonPointer location, Object source) {
        if (!(source instanceof Map)) {
            return null;
        }
        return new Bucket (uri, location, asMap (source));
    }
}
