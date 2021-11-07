/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

/**
 * OpenAPI object wrapper. It provides utility functions to extract the properties from the raw map
 * object.
 */
public class Node {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^x-");

    /** (json) path to this node */
    private final String path;

    /** wrapped OpenAPI object node */
    private final Map<String, Object> properties;

    public static Node empty() {
        return new Node("empty", Collections.emptyMap ());
    }

    @Deprecated
    public Node (Map<String, Object> properties) {
        this.path = "not set";
        this.properties = properties;
    }

    public Node (String path, Map<String, Object> properties) {
        this.path = path;
        this.properties = properties;
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
     * get the raw value of the given property as {@link String}.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable String getStringValue (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return asString (property, value);
    }

    /**
     * same as {@link #getStringValue}, but throws if the property values is {@code null}.
     */
    public String getRequiredStringValue (String property) throws NoValueException {
        final String value = getStringValue (property);
        if (value == null)
            throw new NoValueException (getPath (property));

        return value;
    }

    /**
     * get the raw value of the given property as {@link Boolean}.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable Boolean getBooleanValue (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return checkType (property, value, Boolean.class);
    }

    /**
     * get the raw array values of the given property as collection of {@link String}s.
     *
     * @param property property name
     * @return collection of values
     */
    public @Nullable Collection<String> getStringValues (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return asArray (property, value, String.class);
    }

    /**
     * same as {@link #getStringValues(String)}.
     */
    public @Nullable Collection<String> getArrayStringValues (String property) {
        return getStringValues (property);
    }

    /**
     * get the raw array values of the given property as collection of {@link String}s. If property
     * is missing it returns an empty collection.
     *
     * @param property property name
     * @return collection of values
     */
    public Collection<String> getStringValuesOrEmpty (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return Collections.emptyList ();

        return asArray (property, value, String.class);
    }

    /**
     * same as {@link #getStringValuesOrEmpty(String)}.
     */
    public Collection<String> getArrayStringValuesOrEmpty (String property) {
        return getStringValuesOrEmpty (property);
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
    public <T> Map<String, T> getObjectValues (ObjectFactory<T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();
        getPropertyNames ().forEach (k -> result.put (k, factory.create (getObjectNode (k))));
        return unmodifiableMap (result);
    }

    /**
     * same as {@link #getObjectValues(ObjectFactory)}.
     */
    public <T> Map<String, T> getObjectValuesOrEmpty (ObjectFactory<T> factory) {
        return getObjectValues (factory);
    }

    /**
     * get the object value of the given property as {@link Node}. Throws if the property is not
     * an object.
     *
     * @param property property name
     * @return property value wrapped as {@link Node}.
     */
    @SuppressWarnings ("unchecked")
    Node getObjectNode (String property) {
        final Object value = getRawValue (property);
        if (!isObject (value)) {
            throw new NoObjectException(getPath(property));
        }

        return new Node (getPath (property), (Map<String, Object>) value);
    }

    /**
     * converts the array value of the given property to a collection of {@link Node}s. Throws if
     * it is not an array or the elements are no objects.
     *
     * @param property property name
     * @return collection of {@link Node}s
     */
    public Collection<Node> getObjectNodes (String property) {
        return asArray (property, getRawValue (property));
    }

    /**
     * converts the array value of the given property to a collection of {@link Node}s. If the
     * property is missing or empty it returns an empty collection. Throws if it is not an array.
     *
     * @param property property name
     * @return collection of {@link Node}s
     */
    public Collection<Node> getObjectNodesOrEmpty (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return Collections.emptyList ();

        return asArray (property, value);
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
        if (!hasProperty (property))
            return null;

        return factory.create (getObjectNode (property));
    }

    /**
     * same as {@link #getObjectNode}, but throws if the property values is {@code null}.
     */
    public <T> T getRequiredObjectValue (String property, ObjectFactory<T> factory) {
        final T value = getObjectValue (property, factory);
        if (value == null) {
            throw new NoValueException (getPath(property));
        }
        return value;
    }

    /**
     * converts the value of the given property name to a collection of {@code T}s casting the items
     * to {@code T}s.
     *
     * @param property property name
     * @param <T> target type
     * @return collection of {@code T}s
     */
    public <T> Collection<T> getArrayValues (String property, Class<T> itemType) {
        return asArray (property, getRawValue (property), itemType);
    }

    /**
     * converts the value of the given property name to a collection of {@code T}s casting the items
     * to {@code T}s. If the property is missing it returns an empty collection.
     *
     * @param property property name
     * @param <T> target type
     * @return collection of {@code T}s
     */
    public <T> Collection<T> getArrayValuesOrEmpty (String property, Class<T> itemType) {
        final Object rawValue = getRawValue (property);
        if (rawValue == null)
            return Collections.emptyList ();

        return asArray (property, rawValue, itemType);
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
    public <T> Collection<T> getArrayValues (String property, ObjectFactory<T> factory) {
        return unmodifiableCollection (
            getObjectNodes (property)
                .stream ()
                .map (factory::create)
                .collect (Collectors.toList ()));
    }

    public <T> Collection<T> getArrayValuesOrEmpty (String property, ObjectFactory<T> factory) {
        return unmodifiableCollection (
            getObjectNodesOrEmpty (property)
                .stream ()
                .map (factory::create)
                .collect (Collectors.toList ()));
    }

    /**
     * converts the value of the given property to a map from property name to {@code T} using the
     * given factory to convert all property values to {@code T}.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map of property values to {@code T}s
     */
    @SuppressWarnings ("unchecked")
    public <T> @Nullable Map<String, T> getObjectValues (String property, ObjectFactory<T> factory) {
        Map<String, Object> value = (Map<String, Object>) properties.get (property);
        if (value == null)
            return null;

        return new Node(getPath (property), value).getObjectValues (factory);
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
     * {@link Node}. It traverses into any child map or array of the property.
     *
     * @param property property name
     * @param handler node handler
     */
    public void traverseProperty (String property, NodeHandler handler) {
        final Object value = getRawValue (property);

        if (isObject (value)) {
            handler.handle (getObjectNode (property));
        } else if (isArray (value, Map.class)) {
            getObjectNodes (property).forEach (handler::handle);
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
    public Set<String> getPropertyNames () {
        return properties.keySet ();
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

    private String getPath (String property) {
        return String.format ("%s.%s", path, property);
    }

    private String getArrayPath (String property, int index) {
        return String.format ("%s.%s[%d]", path, property, index);
    }

    @SuppressWarnings ("unchecked")
    private Collection<Object> notNullAsArray (String property, @Nullable Object value) {
        return (Collection<Object>) notNullProperty (property, value);
    }

    private <T> T notNullProperty (String property, @Nullable T value) {
        if (value == null)
            throw new NoValueException (getPath (property));

        return value;
    }

    private String asString (String property, Object value) {
        if (! isString (value))
            throw new TypeMismatchException (getPath(property), String.class);

        return (String) value;
    }

    @SuppressWarnings ("unchecked")
    private <T> Collection<T> asArray (String property, Object value, Class<T> itemType) {
        if (!isArray (value, itemType))
            throw new TypeMismatchException (getPath (property), getArrayTypeName (itemType));

        return (Collection<T>) value;
    }

    @SuppressWarnings ("unchecked")
    private Collection<Node> asArray (String property, Object value) {
        if (!isArray (value))
            throw new NoArrayException (getPath (property));

        final Object[] values = ((Collection<Object>) value).toArray ();

        final Collection<Node> objects = new ArrayList<> ();
        for (int i = 0; i < values.length; i++) {
            Object val = values[i];

            if (!isObject (val)) {
                throw new NoObjectException (getArrayPath (property, i));
            }

            objects.add (new Node (getArrayPath (property, i), (Map<String, Object>) val));
        }

        return unmodifiableCollection (objects);
    }

    private <T> String getArrayTypeName (Class<T> itemType) {
        return String.format ("%s<%s>", Collection.class.getName (), itemType.getName ());
    }

    @SuppressWarnings ("unchecked")
    private <T> boolean isArray (Object value, Class<T> itemType) {
        if (!isArray (value))
            return false;

        final Collection<T> items = (Collection<T>) value;
        for (T item : items) {
            if (! isType (item, itemType))
                return false;
        }
        return true;
    }

    private <T> T checkType (String property, Object value, Class<T> type) {
        if (!type.isInstance (value))
            throw new TypeMismatchException (getPath (property), type);

        return type.cast (value);
    }

    private <T> boolean isType (@Nullable Object value, Class<T> type) {
        return type.isInstance (value);
    }

    private boolean isObject (@Nullable Object value) {
        return value instanceof Map;
    }

    private boolean isArray (@Nullable Object value) {
        return value instanceof Collection;
    }

    private boolean isString (@Nullable Object value) {
        return value instanceof String;
    }
}
