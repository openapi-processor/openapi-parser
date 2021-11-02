/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

/**
 * OpenAPI object wrapper. It provides utility functions to extract the properties from the un-typed
 * object.
 */
public class Node {
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
     * get the raw value of the given property cast to {@link String}.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable String getPropertyAsString (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return castToString (property, value);
    }

    /**
     * get the raw array value of the given property key as collection of {@link String}s.
     *
     * @param property property name
     * @return collection of values
     */
    public @Nullable Collection<String> getPropertyAsStrings (String property) {
        final Object value = getRawValue (property);
        if (value == null)
            return null;

        return castToCollection(property, value, String.class);
    }

    /**
     * same as {@link #getPropertyAsString}, but throws if the property values is {@code null}.
     */
    public String getRequiredPropertyAsString (String property) {
        return notNullProperty (property, getPropertyAsString (property));
    }

    /**
     * converts the properties of this {@link Node} to a map from property name to {@code T} using
     * the given factory to convert all property values to {@code T}.
     *
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map from properties to {@code T}
     */
    @SuppressWarnings ("unchecked")
    public <T> Map<String, T> getPropertiesAsMapOf (NodeConverter<T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();
        properties.forEach ((k, v) -> {
            result.put (k, factory.create (new Node (getPath (k), (Map<String, Object>) v)));
        });
        return unmodifiableMap (result);
    }

    /**
     * get the value of the given property key as {@link Node}.
     *
     * @param property property name
     * @return property value wrapped as {@link Node}.
     */
    public Node getPropertyAsNode (String property) {
        final Object value = properties.get (property);
        if (!isObject (value)) {
            throw new NoObjectException(getPath(property));
        }

        return new Node (getPath (property), notNullAsObject (property, value));
    }

    /**
     * converts the array value of the given property key to a collection of {@link Node}s. If the
     * elements are not maps the result collection will be empty.
     *
     * @param property property name
     * @return collection of {@link Node}s
     */
    @SuppressWarnings ("unchecked")
    public Collection<Node> getPropertyAsNodes (String property) {
        if (!hasProperty (property))
            return Collections.emptyList ();

        final Object value = properties.get (property);
        if (!isArray (value)) {
            throw new NoArrayException (String.format ("property %s should be an array", property));
        }

        final Collection<Node> nodes = new ArrayList<> ();
        final Object[] values = notNullAsArray (property, value).toArray ();
        for (int i = 0; i < values.length ; i++) {
            Object val = values[i];

            if (isObject (val)) {
                nodes.add (new Node (getArrayPath(property, i), (Map<String, Object>) val));
            }
        }

        return unmodifiableCollection (nodes);
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
    public <T> @Nullable T getPropertyAs (String property, NodeConverter<T> factory) {
        if (!hasProperty (property))
            return null;

        return factory.create (getPropertyAsNode (property));
    }

    /**
     * same as {@link #getPropertyAs}, but throws if the property values is {@code null}.
     */
    public <T> T getRequiredPropertyAs (String property, NodeConverter<T> factory) {
        return notNullProperty (property, getPropertyAs (property, factory));
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
    public <T> Collection<T> getPropertyAsArrayOf (String property, NodeConverter<T> factory) {
        return unmodifiableCollection (
            getPropertyAsNodes (property)
                .stream ()
                .map (factory::create)
                .collect (Collectors.toList ()));
    }

    /**
     * converts the value of the given property name to a map from property name to {@code T} using
     * the given factory to convert all property values to {@code T}.
     *
     * @param property property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map of property values to {@code T}s
     */
    @SuppressWarnings ("unchecked")
    public <T> @Nullable Map<String, T> getPropertyAsMapOf (String property, NodeConverter<T> factory) {
        Map<String, Object> value = (Map<String, Object>) properties.get (property);
        if (value == null)
            return null;

        return new Node(getPath (property), value).getPropertiesAsMapOf (factory);
    }

    /**
     * traverses the object tree of the given property and runs the handler on each child
     * {@link Node}. It traverses into any child map or array of the property value.
     *
     * @param property property name
     * @param handler node handler
     */
    public void traverseProperty (String property, NodeHandler handler) {
        final Object value = getRawValue (property);

        if (isObject (value)) {
            handler.handle (getPropertyAsNode (property));
        } else if (isArray (value)) {
            for (Node node : getPropertyAsNodes (property)) {
                handler.handle (node);
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

    private String getPath (String property) {
        return String.format ("%s.%s", path, property);
    }

    private String getArrayPath (String property, int index) {
        return String.format ("%s.%s[%d]", path, property, index);
    }

    @SuppressWarnings ("unchecked")
    private Map<String, Object> notNullAsObject (String property, @Nullable Object value) {
        return (Map<String, Object>) notNullProperty (property, value);
    }

    @SuppressWarnings ("unchecked")
    private Collection<Object> notNullAsArray (String property, @Nullable Object value) {
        return (Collection<Object>) notNullProperty (property, value);
    }

    private <T> T notNullProperty (String property, @Nullable T value) {
        if (value == null)
            throw new NullPropertyException (getPath (property));

        return value;
    }

    private String castToString(String property, Object value) {
        if (!(value instanceof String))
            throw new BadPropertyTypeException (getPath(property), String.class);

        return (String) value;
    }

    @SuppressWarnings ("unchecked")
    private <T> Collection<T> castToCollection (String property, Object value, Class<T> itemType) {
        String type = String.format ("%s<%s>", Collection.class.getName (), itemType.getName ());

        if (!isArray (value))
            throw new BadPropertyTypeException (getPath (property), type);

        // check items
        final Collection<T> collection = (Collection<T>) value;
        for (T item : collection) {
            if (itemType.isInstance (item))
                continue;

            throw new BadPropertyTypeException (getPath (property), type);
        }
        return collection;
    }

    private boolean isObject (@Nullable Object value) {
        return value instanceof Map;
    }

    private boolean isArray (@Nullable Object value) {
        return value instanceof Collection;
    }
}
