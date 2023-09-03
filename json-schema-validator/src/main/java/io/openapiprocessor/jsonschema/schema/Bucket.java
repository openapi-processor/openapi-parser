/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * wraps the properties {@link Map} of a json/yaml object and its location in the source document.
 */
public class Bucket {
    private final Scope scope;
    private final JsonPointer location;
    private final Map<String, Object> properties;

    public static Bucket empty() {
        return createBucket(Scope.empty(), Collections.emptyMap());
    }

    public static @Nullable Bucket createBucket(Scope scope, @Nullable Object source) {
        if (!Types.isObject (source)) {
            return null;
        }

        return new Bucket (scope, Types.asObject (source));
    }

    public static @Nullable Bucket createBucket(Scope scope, @Nullable Object source, JsonPointer location) {
        if (!Types.isObject (source)) {
            return null;
        }

        return new Bucket (scope, location, Types.asObject (source));
    }

    public static Bucket createBucket(Scope scope, Map<String, Object> source) {
        return new Bucket (scope, Types.asObject (source));
    }

    /**
     * create a document "root" bucket with an empty location.
     *
     * @param scope the scope of the document
     * @param properties the document properties
     */
    public Bucket (Scope scope, Map<String, Object> properties) {
        this.scope = scope;
        this.location = JsonPointer.EMPTY;
        this.properties = properties;
    }

    /**
     * create an object bucket with scope &amp; location in the source document and its properties.
     *
     * @param scope the scope of the bucket
     * @param location the location (json pointer) inside {@code source}
     * @param properties the document properties
     */
    public Bucket (Scope scope, JsonPointer location, Map<String, Object> properties) {
        this.scope = scope;
        this.location = location;
        this.properties = properties;
    }

    /**
     * create an object bucket with the object location in the source document and its properties.
     *
     * @param scope the document scope
     * @param location the location (json pointer) inside {@code source}
     * @param properties the document properties
     */
    public Bucket (Scope scope, String location, Map<String, Object> properties) {
        this.scope = scope;
        this.location = JsonPointer.from (location);
        this.properties = properties;
    }

    /**
     * the scope of this bucket.
     *
     * @return the document {@link URI}
     */

    public Scope getScope () {
        return scope;
    }

    public URI getBaseUri () {
        return scope.getBaseUri ();
    }

    public boolean hasId () {
        return scope.getVersion ().getIdProvider ().getId (properties) != null;
    }

    public @Nullable URI getId () {
        String id = scope.getVersion ().getIdProvider ().getId (properties);
        if (id == null)
            return null;

        return UriSupport.createUri (id);
    }

    /**
     * the location of this bucket in the {@code source} document.
     *
     * @return the location
     */
    public JsonPointer getLocation () {
        return location;
    }

    /**
     * convert the property value to the expected type.
     *
     * @param property property name
     * @param converter property converter
     * @param <T> target type
     * @return a {@code T} object or null
     */
    public <T> @Nullable T convert (String property, PropertyConverter<T> converter) {
        return converter.convert (property, getRawValue (property), getLocation (property));
    }

    /**
     * convert "this" bucket to the expected type.
     *
     * @param converter properties converter
     * @param <T> target type
     * @return a {@code T} object or null
     */
    public <T> @Nullable T convert (PropertiesConverter<T> converter) {
        return converter.convert (properties, location.toString ());
    }

    /**
     * get property value as a {@code Bucket}. Throws if the property value is not an object
     * properties {@link Map}.
     *
     * @param property property name
     * @return child bucket
     */
    public @Nullable Bucket getBucket (String property) {
        Object value = getRawValue (property);
        if (value == null)
            return null;

        if (!Types.isMap (value))
            throw new TypeMismatchException (getLocation (property), Map.class);

        return new Bucket (scope, getLocation (property), Types.asMap (value));
    }

    /**
     * get the raw value of the given property.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable Object getRawValue (String property) {
        return properties.get (property);
    }

    /**
     * get the raw value, i.e. the property map, of the bucket.
     *
     * @return the property map
     */
    public Map<String, Object> getRawValues () {
        return properties;
    }

    /**
     * checks if properties contains the given property name.
     *
     * @param property property name
     * @return true if the property exists, else false
     */
    public boolean hasProperty (String property) {
        return properties.containsKey (property);
    }

    public @Nullable Object getProperty (String property) {
        return properties.get (property);
    }

    /**
     * get the raw value at the given property pointer position.
     *
     * @param pointer property location
     * @return property value or null if the property does not exist
     */
//    @Deprecated // replace with getRawValueX
//    public @Nullable Object getRawValue (JsonPointer pointer) {
//        JsonPointer current = JsonPointer.EMPTY;
//        Object value = properties;
//
//        for (String token: pointer.getTokens ()) {
//            current = current.append (token);
//
//            if (isObject (value)) {
//                value = getObjectValue (asMap (value), current);
//
//            } else if (isArray (value)) {
//                value = getArrayValue (asCol (value), current);
//            }
//        }
//
//        return value;
//    }

    /**
     * get the raw value with scope at the given property pointer position.
     *
     * @param pointer property location
     * @return raw value or null if the property does not exist
     */
    public @Nullable RawValue getRawValue (JsonPointer pointer) {
        JsonPointer current = JsonPointer.empty ();
        Object value = properties;
        Scope scope = this.scope;

        for (String token: pointer.getTokens ()) {
            current = current.append (token);

            if (value == properties) {
                Map<String, Object> self = Types.asObject (value);
                value = getObjectValue (self, current);

            } else if (Types.isObject (value)) {
                Map<String, Object> object = Types.asObject (value);

                value = getObjectValue (object, current);
                scope = scope.move (value);

            } else if (Types.isArray (value)) {
                Collection<Object> array = Types.asCol (value);

                value = getArrayValue (array, current);
                scope = scope.move (value);
            }
        }

        return new RawValue (scope, value);
    }

    public @Nullable Object getRawValueValue (JsonPointer pointer) {
        RawValue value = getRawValue (pointer);
        if (value == null)
            return null;

        return value.getValue ();
    }

    private Object getObjectValue (Map<String, Object> object, JsonPointer pointer) {
        Object value = object.get (pointer.tail ());
        if (value == null) {
            throw new NoValueException (pointer.toString ());
        }
        return value;
    }

    private Object getArrayValue (Collection<Object> array, JsonPointer pointer) {
        Object value = array.toArray ()[pointer.tailIndex ()];
        if (value == null) {
            throw new NoValueException(pointer.toString ());
        }
        return value;
    }

    /**
     * walks the object tree of the given property and runs the handler on each child {@code Bucket}.
     * It walks into any child map or collection of the property.
     *
     * @param property property name
     * @param handler node handler
     */
    @Deprecated // ?
    public void walkPropertyTree (String property, BucketVisitor handler) {
        Object value = getRawValue (property);
        JsonPointer propertyLocation = location.append (property);

//        Scope currentScope = getScope (scope, document);
//        Bucket bucket = toBucket (currentScope, document);
//        private @Nullable Bucket toBucket (Scope scope, @Nullable Object source) {
//            if (!(source instanceof Map)) {
//                return null;
//            }
//            return new Bucket (scope, asMap (source));
//        }

        if (Types.isObject (value)) {
//            Scope currentScope = ScopeCalculator.getScope (scope.getUri (), asMap (value));

            handler.visit (nonNull(getBucket (property)));

        } else if (value instanceof Collection) {
            int index = 0;
            for (Object o : (Collection<?>) value) {
                if (!(o instanceof Map))
                    continue;

                handler.visit (new Bucket (
                    scope,
                    propertyLocation.append (index).toString (),
                    Types.asMap (o)));

                index++;
            }
        }
    }

    /**
     * create location string of property relative to the bucket.
     *
     * @param property property name
     * @return the location
     */
    // todo should throw if property is not in the bucket?
    private String getLocation (String property) {
        return location.getJsonPointer (property);
    }

    public void forEach (BiConsumer<String, Object> action) {
        properties.forEach (action);
    }

    public void forEachProperty (Consumer<String> action) {
        properties.keySet().forEach (action);
    }
}
