/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.openapiparser.converter.Types.asMap;
import static java.util.Collections.unmodifiableMap;

/**
 * wraps the properties {@link Map} of a json/yaml object and its location in the source document.
 */
public class Bucket {
    private final @Nullable URI source; // document
    private final JsonPointer location;
    private final Map<String, Object> properties;

    public static Bucket empty() {
        return new Bucket (Collections.emptyMap ());
    }

    private Bucket (Map<String, Object> properties) {
        this.source = null;
        this.location = JsonPointer.EMPTY;
        this.properties = properties;
    }

    @Deprecated
    public Bucket (JsonPointer location, Map<String, Object> properties) {
        this.source = null;
        this.location = location;
        this.properties = properties;
    }

    /**
     * create a document "root" bucket with an empty location.
     *
     * @param source the document URI
     * @param properties the document properties
     */
    @SuppressWarnings ("NullableProblems")
    public Bucket (URI source, Map<String, Object> properties) {
        this.source = source;
        this.location = JsonPointer.EMPTY;
        this.properties = properties;
    }

    /**
     * create an object bucket with the object location in the source document and its properties.
     *
     * @param source the document URI
     * @param location the location (json pointer) inside {@code source}
     * @param properties the document properties
     */
    @SuppressWarnings ("NullableProblems")
    public Bucket (URI source, JsonPointer location, Map<String, Object> properties) {
        this.source = source;
        this.location = location;
        this.properties = properties;
    }

    /**
     * create an object bucket with the object location in the source document and its properties.
     *
     * @param source the document URI
     * @param location the location (json pointer) inside {@code source}
     * @param properties the document properties
     */
    @SuppressWarnings ("NullableProblems")
    public Bucket (URI source, String location, Map<String, Object> properties) {
        this.source = source;
        this.location = JsonPointer.fromJsonPointer (location);
        this.properties = properties;
    }

    /**
     * the document uri of this bucket.
     *
     * @return the document {@link URI}
     */
    public @Nullable URI getSource () {
        return source;
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

        if (!(value instanceof Map))
            throw new TypeMismatchException (getLocation (property), Map.class);

        return new Bucket (source, getLocation (property), asMap (value));
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
        return unmodifiableMap (properties);
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

    // todo use converter?? implement here
    public @Nullable Object getProperty (JsonPointer location) {
        return location.getValue (properties);

        //        if (!isPath (path)) {
//            return getRawValue (path);
//        } else {
//            String[] parts = getParts (path);
//            int last = parts.length - 1;
//
//            Node node = root;
//            for (int i = 0; i < parts.length; i++) {
//                String part = parts[i];
//
//                if (node == null || !node.hasProperty (part))
//                    return null;
//
//                if (i == last)
//                    return node.getRawValue (part);
//
//                node = node.getNode (part);
//            }
//
//            return null;
//        }
    }

    /**
     * walks the object tree of the given property and runs the handler on each child {@code Bucket}.
     * It walks into any child map or collection of the property.
     *
     * @param property property name
     * @param handler node handler
     */
    public void walkPropertyTree (String property, BucketVisitor handler) {
        Object value = getRawValue (property);
        JsonPointer propertyLocation = location.append (property);

        if (value instanceof Map) {
            handler.visit (getBucket (property));

        } else if (value instanceof Collection) {
            int index = 0;
            for (Object o : (Collection<?>) value) {
                if (!(o instanceof Map))
                    continue;

                handler.visit (new Bucket (
                    source,
                    propertyLocation.append (index).toString (),
                    asMap (o)));

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

/*
    @Nullable Object find(String path) {
        if (!isPath (path)) {
            return root.getRawValue (path);
        } else {
            String[] parts = getParts (path);
            int last = parts.length - 1;

            Node node = root;
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];

                if (node == null || !node.hasProperty (part))
                    return null;

                if (i == last)
                    return node.getRawValue (part);

                node = node.getNode (part);
            }

            return null;
        }
    }

    private boolean isPath (String path) {
        return path.startsWith (SLASH);
    }

    private String[] getParts (String path) {
        return path.substring (1).split (SLASH);
    }
 */
