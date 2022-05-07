/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.Types;
import io.openapiparser.validator.support.Equals;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;

public class JsonInstance {
    private final JsonInstanceContext context;

    private final @Nullable Object value;
    private final JsonPointer location;

    public JsonInstance (@Nullable Object value, JsonInstanceContext context) {
        this.context = context;
        this.value = value;
        this.location = JsonPointer.EMPTY;
    }

    public JsonInstance (JsonPointer location, @Nullable Object value, JsonInstanceContext context) {
        this.context = context;
        this.value = value;
        this.location = location;
    }

    public URI getScope () {
        return context.getScope ();
    }

    public JsonPointer getLocation () {
        return location;
    }

    public String getPath () {
        return location.toString ();
    }

    public @Nullable Object getRawValue () {
        return value;
    }

    public JsonInstance getPropertyName (String propertyName) {
        if (!isObject ())
            throw new RuntimeException(); // todo

        JsonPointer propertyPointer = location.append (propertyName);

        return new JsonInstance (propertyPointer, propertyName, context);
    }

    public JsonInstance getValue (String property) {
        if (!isObject ())
            throw new RuntimeException(); // todo

        JsonPointer propertyPointer = location.append (property);
        Object propertyValue = asObject ().get (property);

        return new JsonInstance (propertyPointer, propertyValue, context);
    }

    public JsonInstance getValue (int idx) {
        if (!isArray ())
            throw new RuntimeException(); // todo

        JsonPointer idxPointer = location.append (idx);
        Object[] items = asCol (value).toArray ();

        return new JsonInstance (idxPointer, items[idx], context);
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

    public URI getRefKey () {
        return context.getReferenceKey (getRef ());
    }

    public JsonInstance getRefInstance () {
        URI ref = getRef ();
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

    public @Nullable String asString () {
        return Types.asString (value);
    }

    public @Nullable Number asNumber () {
        return Types.asNumber (value);
    }

    public boolean isNull () {
        return value == null;
    }

    public boolean isString () {
        return value instanceof String;
    }

    public boolean isNumber () {
        return value instanceof Number;
    }

    public boolean isObject () {
        return value instanceof Map;
    }

    public boolean isArray () {
        return value instanceof Collection;
    }

    public boolean isEqual (JsonInstance other) {
        return Equals.equals (value, other.value);
    }

    @Override
    public String toString () {
        String location = this.location.toString ();
        if (location == null) {
            return String.format ("%s", context.getScope ());
        }
        return String.format ("%s", this.location);
    }

    private @Nullable URI getRef () {
        Map<String, Object> object = asObject ();
        Object ref = object.get (Keywords.REF);
        if (!Types.isString (ref))
            return null;

        return UriSupport.createUri ((String)ref);
    }
}
