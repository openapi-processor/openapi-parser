/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.TypeMismatchException;
import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.validator.support.Equals;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

public class JsonInstance {
    private final @Nullable Object value;
    private final JsonPointer location;

    public JsonInstance (@Nullable Object value) {
        this.value = value;
        this.location = JsonPointer.EMPTY;
    }

    public JsonInstance (JsonPointer location, @Nullable Object value) {
        this.value = value;
        this.location = location;
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
            getPropertyKey (propertyName));
    }

    public JsonInstance getValue (String property) {
        return new JsonInstance (
            location.append (property),
            getPropertyValue (property));
    }

    public JsonInstance getValue (int idx) {
        return new JsonInstance (
            location.append (idx),
            getArrayValue (idx));
    }

    public int getArraySize () {
        return getArraySizeX ();
    }

    public @Nullable Map<String, Object> asObject () {
        return Types.asMap (value);
    }

    public @Nullable Collection<Object> asCollection () {
        return Types.asCol (value);
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
        return String.format ("%s", this.location != null ? this.location : "/");
    }

    private @Nullable String getPropertyKey (String property) {
        if (value == null || !isObject ())
            throw new TypeMismatchException (location.append (property), Map.class);

        Map<String, Object> object = Types.asMap (value);
        if (!object.containsKey (property))
            throw new InvalidPropertyException (location.append (property));

        return property;
    }

    private @Nullable Object getPropertyValue (String property) {
        if (value == null || !isObject ())
            throw new TypeMismatchException (location.append (property), Map.class);

        Map<String, Object> object = Types.asMap (value);
        return object.get (property);
    }

    private @Nullable Object getArrayValue (int idx) {
        if (value == null || !isArray ())
            throw new TypeMismatchException (location, Collection.class);

        Object[] items = Types.asCol (value).toArray ();
        return items[idx];
    }

    private int getArraySizeX () {
        if (value == null || !isArray ())
            throw new TypeMismatchException (location, Collection.class);

        return Types.asCol (value).size ();
    }
}
