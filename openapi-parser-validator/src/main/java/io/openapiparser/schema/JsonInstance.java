/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.validator.support.Equals;

import java.util.*;

import static io.openapiparser.converter.Types.*;

public class JsonInstance {
    private final JsonInstanceContext context;

    private final Object root;
    private final Object value;
    private final JsonPointer valuePointer;

    public JsonInstance (Object value, JsonInstanceContext context) {
        this.context = context;
        this.root = value;
        this.value = value;
        this.valuePointer = JsonPointer.EMPTY;
    }

    public JsonInstance (Object root, JsonPointer valuePointer, Object value, JsonInstanceContext context) {
        this.context = context;
        this.root = root;
        this.value = value;
        this.valuePointer = valuePointer;
    }

    public JsonPointer getPointer () {
        return valuePointer;
    }

    public String getPath () {
        return valuePointer.toString ();
    }

    public Object getRawValue () {
        return value;
    }

    /*
    // Nullable ???
    public JsonInstance getValue (URI uri) {
        String pointer = uri.getFragment ();
        if (pointer == null || pointer.isEmpty ())
            return this;

        return new JsonInstance (root, uri, getValue (pointer), context);
    }

JsonInstance
    private  Object getValue (@Nullable String path) {
//        if (path == null || path.isEmpty ())
//            return this;

        if (isObject ()) {
            Bucket bucket = new Bucket (asMap (value));
            Object target = bucket.getRawValue (from (path));
            return target;
//            Object target = bucket.getRawValue (from (path));
//            return new JsonInstance (target, context);

        } else if (isArray ()) {
            Object[] items = asCol (value).toArray ();
            int idx = from (path).tailIndex ();
            return items[idx];
//            return new JsonInstance (items[idx], context);
        }

        // todo throw
        return this;
    }
    */

    public JsonInstance getValue (String property) {
        if (!isObject ())
            throw new RuntimeException(); // todo

        JsonPointer propertyPointer = valuePointer.append (property);
        Object propertyValue = asObject ().get (property);

        return new JsonInstance (root, propertyPointer, propertyValue, context);
    }

    public JsonInstance getValue (int idx) {
        if (!isArray ())
            throw new RuntimeException(); // todo

        JsonPointer idxPointer = valuePointer.append (idx);
        Object[] items = asCol (value).toArray ();

        return new JsonInstance (root, idxPointer, items[idx], context);
    }

    public int getArraySize () {
        if (!isArray ())
            throw new RuntimeException(); // todo

        return asCollection ().size ();
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

    public boolean isNumber () { return value instanceof Number; }

    public boolean isObject () {
        return value instanceof Map;
    }

    public boolean isArray () {
        return value instanceof Collection;
    }

    public boolean isEqual (JsonInstance other) {
        return Equals.equals (value, other.value);
    }
}
