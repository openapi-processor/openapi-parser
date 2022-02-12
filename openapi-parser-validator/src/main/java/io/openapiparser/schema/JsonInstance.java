/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.schema.JsonPointer.from;

public class JsonInstance {
    private final JsonInstanceContext context;
    private final Object value;

    public JsonInstance (Object value, JsonInstanceContext context) {
        this.context = context;
        this.value = value;
    }

    public Object getRawValue () {
        return value;
    }

    public JsonInstance getValue (@Nullable String path) {
        if (path == null || path.isEmpty ())
            return this;

        if (isObject ()) {
            Bucket bucket = new Bucket (asMap (value));
            Object target = bucket.getRawValue (from (path));
            return new JsonInstance (target, context);

        } else if (isArray ()) {
            Object[] items = asCol (value).toArray ();
            int idx = from (path).tailIndex ();
            return new JsonInstance (items[idx], context);
        }

        return this;
    }

    public JsonInstance getValue (int idx) {
        if (!isArray ())
            throw new RuntimeException(); // todo

        Object[] items = asCol (value).toArray ();
        return new JsonInstance (items[idx], context);
    }

    public JsonInstance getRefInstance () {
        Map<String, Object> object = asObject ();
        String ref = as(object.get ("$ref"));

        //context.getRef();
        //        return context.getRef();
        return null;
    }

    public Map<String, Object> asObject () {
        return asMap (value);
    }

    public Collection<Object> asCollection () {
        return asCol (value);
    }

    public String asString () {
        return as (value);
    }

    public Number asNumber () {
        return as (value);
    }

    public boolean isNull () {
        return value == null;
    }

    public boolean isObject () {
        return value instanceof Map;
    }

    public boolean isArray () {
        return value instanceof Collection;
    }
}
