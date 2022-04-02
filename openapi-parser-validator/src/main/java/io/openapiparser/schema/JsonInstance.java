/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.validator.support.Equals;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
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

    public URI getScope () {
        return context.getScope ();
    }

    // todo rename to getLocation()
    public JsonPointer getPointer () {
        return valuePointer;
    }

    public String getPath () {
        return valuePointer.toString ();
    }

    public Object getRawValue () {
        return value;
    }

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

    public boolean isRef () {
        if (!isObject ())
            return false;

        URI ref = getRef ();
        if (ref == null)
            return false;

        return context.isRef (ref);
    }

    public JsonInstance getRefInstance () {
        if (!isRef ())
            throw new RuntimeException (); // todo

        URI ref = getRef ();
        context.visitRef (ref);

        Reference reference = context.getReference (ref);
        JsonInstanceContext refContext = context.withScope (reference.getValueScope ());
        Object rawValue = reference.getRawValue ();
        Map<String, Object> props = asMap(rawValue);
        return new JsonInstance (props, refContext.withId (props));
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

    private @Nullable URI getRef () {
        Map<String, Object> object = asObject ();
        Object ref = object.get (Keywords.REF);
        if (!isString (ref))
            return null;

        return URI.create ((String)ref);
    }
}
