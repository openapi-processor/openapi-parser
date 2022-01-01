/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

import static io.openapiparser.Type.*;
import static io.openapiparser.schema.JsonPointer.fromJsonPointer;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

import io.openapiparser.converter.NoValueException;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * OpenAPI object wrapper. It provides utility functions to extract the properties from the raw map
 * object.
 */
@Deprecated
public class Node {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^x-");

    /** (json) path to this node */
    private final String path;

    /** wrapped OpenAPI object node */
    private final Map<String, Object> properties;

    public static Node empty() {
        return new Node("empty", Collections.emptyMap ());
    }

    public Node (String path, Map<String, Object> properties) {
        this.path = path;
        this.properties = new HashMap<> (properties);
    }

    public PropertyBucket toBucket () {
        String fragment = URI.create (path).getRawFragment ();
        return new PropertyBucket (fromJsonPointer (fragment), properties);
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

    // todo: temporay
    public @Nullable  Map<String, Object> getRawValue () {
        return properties;
    }

    /**
     * get the raw value of the given property as {@link String}.
     *
     * @param property property name
     * @return property value or null if the property is missing
     */
    public @Nullable String getStringValue (String property) {
        return convertOrNull (getPath (property), getRawValue (property), String.class);
    }

    /**
     * same as {@link #getStringValue(String)}, but throws if the property values is {@code null}.
     *
     * @param property property name
     * @return property value or null if the property is missing
     */
    public String getRequiredStringValue (String property) throws NoValueException {
        return convertOrThrow (getPath (property), getRawValue (property), String.class);
    }

    /**
     * get the raw value of the given property as {@link Boolean}.
     *
     * @param property property name
     * @return property value or null if the property is missing
     */
    public @Nullable Boolean getBooleanValue (String property) {
        return convertOrNull (getPath (property), getRawValue (property), Boolean.class);
    }

    /**
     * same as {@link #getBooleanValue(String)}, but returns the fallback if the property values is
     * missing.
     *
     * @param property property name
     * @param fallback fallback value
     * @return property value or fallback if the property is missing
     */
    public Boolean getBooleanValue (String property, boolean fallback) {
        return convertOrFallback (getPath (property), getRawValue (property), Boolean.class, fallback);
    }

    /**
     * get the raw value of the given property as {@link Number}.
     *
     * @param property property name
     * @return property value or null if the property is missing
     */
    public @Nullable Number getNumberValue (String property) {
        return convertOrNull (getPath (property), getRawValue (property), Number.class);
    }

    /**
     * same as {@link #getNumberValue(String)}, but returns the fallback if the property values is
     * missing.
     *
     * @param property property name
     * @param fallback fallback value
     * @return property value or fallback if the property is missing
     */
    public Number getNumberValue (String property, Number fallback) {
        return convertOrFallback (getPath (property), getRawValue (property), Number.class, fallback);
    }

    /**
     * get the raw value of the given property as {@link Integer}.
     *
     * @param property property name
     * @return property value or null if the property is missing
     */
    public @Nullable Integer getIntegerValue (String property) {
        return convertOrNull (getPath (property), getRawValue (property), Integer.class);
    }

    /**
     * same as {@link #getIntegerValue(String)}, but returns the fallback if the property values is
     * missing.
     *
     * @param property property name
     * @param fallback fallback value
     * @return property value or fallback if the property is missing
     */
    public Integer getIntegerValue (String property, Integer fallback) {
        return convertOrFallback (getPath (property), getRawValue (property), Integer.class, fallback);
    }

    /**
     * converts the value of the given property to a {@code T} using the given factory to convert
     * the value to {@code T}.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> @Nullable T getObjectValue (String property, ObjectFactory<T> factory) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return factory.create (new Node (property, convertMap (getPath(property), value)));
    }

    /**
     * same as {@link #getObjectValue(String, ObjectFactory)}, but throws if the property values is
     * {@code null}.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> T getRequiredObjectValue (String property, ObjectFactory<T> factory) {
        final T value = getObjectValue (property, factory);
        if (value == null) {
            throw new NoValueException (getPath(property));
        }
        return value;
    }

    /**
     * get the raw array values of the given property as {@link Collection<String>} or null if the
     * property is missing.
     *
     * @param property property name
     * @return property collection or null if the property is missing
     */
    public @Nullable Collection<String> getStringValues (String property) {
        return convertCollectionOrNull (getPath (property), getRawValue (property), String.class);
    }

    /**
     * get the raw array values of the given property as {@link Collection<String>} or an empty
     * collection if the property value is missing.
     *
     * @param property property name
     * @return collection of values
     */
    public Collection<String> getStringValuesOrEmpty (String property) {
        return convertCollectionOrEmpty (getPath (property), getRawValue (property), String.class);
    }

    /**
     * get the raw value of the given property as {@link Map<String, String>} or an empty map if the
     * property value is missing.
     *
     * @param property property name
     * @return map of values
     */
    public Map<String, String> getMapStringValuesOrEmpty (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return Collections.emptyMap ();

        return convertMap (getPath (property), value, String.class);
    }

    /**
     * converts the value of the given property name to a map of {@link String} to {@link Set<String>}.
     *
     * @param property property name
     * @return map of property to set of property string values
     */
    public Map<String, Set<String>> getObjectSetValuesOrEmpty (String property) {
        Object value = getRawValue (property);
        if (value == null) {
            return Collections.emptyMap ();
        }

        final String propertyPath = getPath(property);
        final Map<String, Object> propertyValues = convertMap (propertyPath, value);

        Map<String, Set<String>> required = new LinkedHashMap<> ();

        propertyValues.forEach ((itemProp, itemValue) -> {
            final Collection<String> values = convertCollectionOrEmpty (
                getPath (propertyPath, itemProp), itemValue, String.class);
            required.put (itemProp, unmodifiableSet (new LinkedHashSet<> (values)));
        });

        return unmodifiableMap (required);
    }

    /**
     * converts the value of the given property name to a collection of {@code T}s using the given
     * factory to convert all property values to {@code T}s.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> Collection<T> getObjectValues (String property, ObjectFactory<T> factory) {
        return unmodifiableCollection (
            getNodes (property)
                .stream ()
                .map (factory::create)
                .collect (toList ()));
    }

    /**
     * converts the value of the given property name to a collection of {@code T}s using the given
     * factory to convert all property values to {@code T}s. Returns an empty collection if the
     * property is missing.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> Collection<T> getObjectValuesOrEmpty (String property, ObjectFactory<T> factory) {
        final Collection<Node> nodes = getNodesOrEmpty (property);
        if (nodes == null)
            return Collections.emptyList ();

        return unmodifiableCollection (
            nodes
                .stream ()
                .map (factory::create)
                .collect (toList ()));
    }

    /**
     * converts the properties of this {@link Node} to a map from property name to {@code T} using
     * the given factory to convert all property values to {@code T}. If the property is missing
     * it returns an empty map.
     *
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map from properties to {@code T}
     */
    public <T> Map<String, T> getMapObjectValuesOrEmpty (ObjectFactory<T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();

        properties.forEach ((itemProp, itemValue) -> {
            String itemPath = getPath (path, itemProp);
            result.put (itemProp, factory.create (new Node (itemPath, convertMap (itemPath, itemValue))));
        });

        return unmodifiableMap (result);
    }

    /**
     * converts the value of the given property to a map of {@link String} to {@code T} using the
     * given factory to convert all property values to {@code T}s.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map of {@link String} to {@code T}
     * *
     * same as {@link #getMapObjectValues}, but returns an empty map if the property values is
     * {@code null}.
     */
    public <T> Map<String, T> getMapObjectValuesOrEmpty (String property, ObjectFactory<T> factory) {
        final Object value = getRawValue (property);
        if (value == null)
            return Collections.emptyMap ();

        return getMapObjectValues (getPath (property), value, factory);
    }

    /**
     * converts the value of the given value to a map of {@link String} to {@code T} using the given
     * factory to convert all property values to {@code T}s.
     *
     * @param path the node path
     * @param value source map value
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map of {@link String} to {@code T}
     */
    public <T> Map<String, T> getMapObjectValues (String path, Object value, ObjectFactory<T> factory) {
        final Map<String, T> result = new LinkedHashMap<> ();

        final Map<String, Object> parent = convertMap (path, value);
        parent.forEach ((itemProp, itemValue) -> {
            String itemPath = getPath (path, itemProp);
            result.put (itemProp, factory.create (new Node (itemPath, convertMap (itemPath, itemValue))));
        });

        return unmodifiableMap (result);
    }

    /**
     * get the object value of the given property as {@link Node}. Throws if the property is not
     * an object.
     *
     * @param property property name
     * @return property value wrapped as {@link Node}.
     */
    Node getNode (String property) {
        return new Node (property, convertMap (getPath(property), getRawValue (property)));
    }

    /**
     * converts the array value of the given property to a collection of {@link Node}s. Throws if
     * it is not a collection or the elements are no objects.
     *
     * @param property property name
     * @return collection of {@link Node}s
     */
    Collection<Node> getNodes (String property) {
        return getNodes (getPath(property), getRawValue (property));
    }

    /**
     * converts the array value of the given property to a collection of {@link Node}s. If the
     * property is missing or empty it returns an empty collection. Throws if it is not an array
     * or the items or not a map.
     *
     * @param property property name
     * @return collection of {@link Node}s
     */
    @Nullable Collection<Node> getNodesOrEmpty (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return Collections.emptyList ();

        return getNodes (getPath (property), value);
    }

    /**
     * converts the array value of the given value to a collection of {@link Node}s. Throws if
     * it is not a collection or the elements are no objects.
     *
     * @param path node path
     * @param value the array
     * @return collection of {@link Node}s
     */
    private Collection<Node> getNodes (String path, @Nullable Object value) {
        return convertCollection (path, value, (index, item) -> {
            String itemPath = getPath (path, index);
            return new Node (itemPath, convertMap (itemPath, item));
        });
    }

    /**
     * get a map with the extension properties.
     *
     * @return map of extension properties
     */
    public Map<String, Object> getExtensions () {
        Map<String, Object> extensions = new LinkedHashMap<> ();
        properties.forEach ((property, value) -> {
            if (isExtension (property)) {
                extensions.put (property, value);
            }
        });
        return extensions;
    }

    private boolean isExtension (String property) {
        return EXTENSION_PATTERN.matcher(property).find ();
    }

    /**
     * traverses the object tree of the given property and runs the handler on each child
     * {@link Node}. It traverses into any child map or collection of the property.
     *
     * @param property property name
     * @param handler node handler
     */
    public void traverseProperty (String property, NodeHandler handler) {
        final Object value = getRawValue (property);

        if (value instanceof Map) {
            handler.handle (getNode (property));

        } else if (value instanceof Collection) {
            for (Object o : (Collection<?>) value) {
                if (!(o instanceof Map))
                    continue;

                handler.handle (new Node (getPath (property), uncheckedMap (o)));
            }
        }
    }

    /**
     * checks if the {@link Node} contains the given property name.
     *
     * @param property property name
     * @return true if the property exists, else false
     */
    public boolean hasProperty (String property) {
        return properties.containsKey (property);
    }

    /**
     * a collection of all existing properties in this {@link Node}.
     *
     * @return existing property names
     */
    @SuppressWarnings ("return") // @KeyFor
    public Set<String> getPropertyNames () {
        return properties.keySet();
    }

    /**
     * the count of properties in this {@link Node}.
     *
     * @return count of properties
     */
    int getCountProperties () {
        return properties.size ();
    }

    public String getPath () {
        return path;
    }

    public String getPath (String property) {
        return String.format ("%s/%s", path, property);
    }

    private String getPath (String parent, int index) {
        return String.format ("%s/%d", parent, index);
    }

    private String getPath (String parent, String item) {
        return String.format ("%s/%s", parent, item);
    }

    private String getCollectionPath (String property, int index) {
        return String.format ("%s.%s[%d]", path, property, index);
    }

    private <T> String getCollectionTypeName (Class<T> itemType) {
        return String.format ("%s<%s>", Collection.class.getName (), itemType.getName ());
    }

    @SuppressWarnings ("unchecked")
    private static Map<String, Object> uncheckedMap (Object o) {
        return (Map<String, Object>) o;
    }

    @SuppressWarnings ("unchecked")
    private static <T> T unchecked (Object o) {
        return (T) o;
    }
}
