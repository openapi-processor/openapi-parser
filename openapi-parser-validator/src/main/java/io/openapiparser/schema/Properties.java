/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.PropertiesConverter;
import io.openapiparser.converter.TypeConverter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * .. todo
 */
// todo PropertyContainer
    // PropertyBucket
public class Properties {
    private final URI source;
    private final JsonPointer location;
    private final Map<String, Object> properties;

    public static Properties empty() {
        return new Properties (Collections.emptyMap ());
    }

    @Deprecated
    public Properties (Map<String, Object> properties) {
        this.source = null;
        this.location = JsonPointer.EMPTY;
        this.properties = properties;
    }

    public Properties (JsonPointer location, Map<String, Object> properties) {
        this.source = null;
        this.location = location;
        this.properties = properties;
    }

    public Properties (URI source, Map<String, Object> properties) {
        this.source = source;
        this.location = JsonPointer.fromFragment (source.getRawFragment ());
        this.properties = properties;
    }

    /**
     * convert the property value to the expected type.
     *
     * @param property property name
     * @param converter property converter
     * @param <T> target type
     * @return a {@code T} object or null
     */
    public <T> @Nullable T convert (String property, TypeConverter<T> converter) {
        return converter.convert (property, getRawValue (property), getLocation (property));
    }

    public <T> @Nullable T convert (PropertiesConverter<T> converter) {
        return converter.convert (properties, location.toString ());
    }

    public int getCount() {
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
    public @Nullable Object findProperty (JsonPointer location) {
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
     */
    public void traverseProperty (String property, PropertiesHandler handler) {
        Object value = getRawValue (property);
        JsonPointer propertyLocation = location.append (property);

        if (value instanceof Map) {
            handler.handle (new Properties (propertyLocation, (Map<String, Object>) value));

        } else if (value instanceof Collection) {
            int index = -1;
            for (Object o : (Collection<?>) value) {
                index++;

                if (!(o instanceof Map))
                    continue;

                JsonPointer itemLocation = propertyLocation.append (String.valueOf (index));
                handler.handle (new Properties (itemLocation, (Map<String, Object>) o));
            }
        }
    }

    // todo named consumer???
    public void forEach (BiConsumer<String, Object> action) {
        properties.forEach (action);
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
