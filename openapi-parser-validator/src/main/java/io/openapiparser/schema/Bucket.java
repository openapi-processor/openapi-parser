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

/**
 * wraps the properties {@link Map} of a json/yaml object and its location.
 */
public class Bucket {
    private final URI source;
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
    public Bucket (String location, Map<String, Object> properties) {
        this.source = null;
        this.location = JsonPointer.fromJsonPointer (location);
        this.properties = properties;
    }

    @Deprecated
    public Bucket (JsonPointer location, Map<String, Object> properties) {
        this.source = null;
        this.location = location;
        this.properties = properties;
    }

    @Deprecated
    public Bucket (URI source, Map<String, Object> properties) {
        this.source = source;
        this.location = JsonPointer.fromFragment (source.getRawFragment ());
        this.properties = properties;
    }

    public Bucket (URI source, String location, Map<String, Object> properties) {
        this.source = source;
        this.location = JsonPointer.fromJsonPointer (location);
        this.properties = properties;
    }

    public URI getSource () {
        return source;
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

    public <T> @Nullable T convert (PropertiesConverter<T> converter) {
        return converter.convert (properties, location.toString ());
    }

    public @Nullable Bucket getBucket (String property) {
        Object value = getRawValue (property);
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (getLocation (property), Map.class);

        return new Bucket (source, getLocation (property), asMap (value));
    }

    public int getSize () {
        return properties.size ();
    }

    private String getLocation (String property) {
        return location.getJsonPointer (property);
    }

//    @Deprecated
//    public @Nullable Content getContentValue (JsonPointer pointer, String property) {
//        JsonPointer propertyPointer = pointer.append (property);
//        Map<String, Object> raw = getRawMapValue (propertyPointer, property);
//        if (raw == null)
//            return null;
//
//        return new Content (raw);
//    }

    /**
     * get the raw value of the given property.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable Object getRawValue (String property) {
        return properties.get (property);
    }

    @Deprecated
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

    // todo use converter??
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
     * traverses the object tree of the given property and runs the handler on each child
     * {@link Node}. It traverses into any child map or collection of the property.
     *
     * @param property property name
     * @param handler node handler
     */ // to walkProperty
    public void traverseProperty (String property, PropertiesHandler handler) {
        Object value = getRawValue (property);
        JsonPointer propertyLocation = location.append (property);

        if (value instanceof Map) {
            handler.handle (new Bucket (propertyLocation, (Map<String, Object>) value));

        } else if (value instanceof Collection) {
            int index = -1;
            for (Object o : (Collection<?>) value) {
                index++;

                if (!(o instanceof Map))
                    continue;

                JsonPointer itemLocation = propertyLocation.append (String.valueOf (index));
                handler.handle (new Bucket (itemLocation, (Map<String, Object>) o));
            }
        }
    }

    public void forEach (BiConsumer<String, Object> action) {
        properties.forEach (action);
    }

    public void forEachProperty (Consumer<String> action) {
        properties.keySet().forEach (action);
    }

    /*
        public void resolve(BiFunction<URI, String, Object> resolver) {
        references.forEach ((key, ref) -> {
            replace (key, ref, resolver.apply (ref.getDocUri (), ref.getRef ()));
        });
    }
     */
//    private @Nullable Map<String, Object> getRawMapValue (JsonPointer pointer, String property) {
//        return Types.convertMapOrNull (pointer.toString (), properties.get (property));
//    }
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
