/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.TypeMismatchException;
import io.openapiparser.converter.Types;
import io.openapiparser.validator.support.Equals;
import org.checkerframework.checker.nullness.qual.*;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.support.Nullness.nonNull;

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
        return new JsonInstance (
            location.append (propertyName),
            getPropertyKey (propertyName),
            context);
    }

    public JsonInstance getValue (String property) {
        return new JsonInstance (
            location.append (property),
            getPropertyValue (property),
            context);
    }

    public JsonInstance getValue (int idx) {
        return new JsonInstance (
            location.append (idx),
            getArrayValue (idx),
            context);
    }

    public int getArraySize () {
        return getArraySizeX ();
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
        URI ref = getRef ();
        if (ref == null)
            throw new RuntimeException (); // todo

        return context.getReferenceKey (ref);
    }

    public JsonInstance getRefInstance () {
        URI ref = getRef ();
        if (ref == null) {
            throw new RuntimeException (); // todo
        }

        Reference reference = context.getReference (ref);
        JsonInstanceContext refContext = context.withScope (reference.getValueScope ());
        Object rawValue = reference.getRawValue ();
        Map<String, Object> props = asMap(rawValue);
        return new JsonInstance (props, refContext.withId (props));
    }

    public @Nullable Map<String, Object> asObject () {
        return asMap (value);
    }

    public @Nullable Collection<Object> asCollection () {
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

    @EnsuresNonNullIf (expression = "value", result = true)
    public boolean isString () {
        return value instanceof String;
    }

    @EnsuresNonNullIf (expression = "value", result = true)
    public boolean isNumber () {
        return value instanceof Number;
    }

    @EnsuresNonNullIf (expression = "value", result = true)
    public boolean isObject () {
        return value instanceof Map;
    }

    @EnsuresNonNullIf (expression = "value", result = true)
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
        Map<String, Object> object = nonNull(asObject ());
        Object ref = object.get (Keywords.REF);
        if (!Types.isString (ref))
            return null;

        return UriSupport.createUri ((String)nonNull(ref));
    }

    private @Nullable String getPropertyKey (String property) {
        if (value == null || !isObject ())
            throw new TypeMismatchException (location.append (property).toString (), Map.class);

        Map<String, Object> object = asMap (value);
        return property;
    }

    private @Nullable Object getPropertyValue (String property) {
        if (value == null || !isObject ())
            throw new TypeMismatchException (location.append (property).toString (), Map.class);

        Map<String, Object> object = asMap (value);
        return object.get (property);
    }

    private @Nullable Object getArrayValue (int idx) {
        if (value == null || !isArray ())
            throw new TypeMismatchException (location.toString (), Collection.class);

        Object[] items = asCol (value).toArray ();
        return items[idx];
    }

    private int getArraySizeX () {
        if (value == null || !isArray ())
            throw new TypeMismatchException (location.toString (), Collection.class);

        return asCol (value).size ();
    }
}
