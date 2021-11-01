/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OpenAPI object wrapper. It provides utility functions to extract the properties from the un-typed
 * object.
 */
public class Node {
    /** wrapped OpenAPI object node */
    private final Map<String, Object> properties;

    public static Node empty() {
        return new Node(Collections.emptyMap ());
    }

    public Node (Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * get the raw/untyped value of the given property key.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    public @Nullable Object getProperty (String property) {
        return properties.get (property);
    }

    /**
     * get the raw/untyped value of the given property key as {@link String}.
     *
     * @param key property name
     * @return property value or null if the property does not exist
     */
    public @Nullable String getPropertyAsString (String key) {
        return (String) properties.get (key);
    }

    /**
     * same as {@link #getPropertyAsString}, but throws if the property values is {@code null}.
     */
    public String getRequiredPropertyAsString (String key) {
        return notNullProperty (key, getPropertyAsString (key));
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
    public <T> Map<String, T> getMapAs (NodeConverter<T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();
        properties.forEach ((k, v) -> {
            result.put (k, factory.create (new Node ((Map<String, Object>) v)));
        });
        return result;
    }

    /**
     * get the value of the given property key as {@link Node}.
     *
     * @param key property name
     * @return property value wrapped as {@link Node}.
     */
    @SuppressWarnings ("unchecked")
    public @Nullable Node getChildNode (String key) {
        if (!properties.containsKey (key))
            return null;

        // todo check map, else throw
        return new Node ((Map<String, Object>) properties.get (key));
    }

    /**
     * traverses the object tree of the given property and runs the handler on each child
     * {@link Node}. It traverses into any child map or array of the property value.
     *
     * @param property property name
     * @param handler node handler
     */
    public void traverseProperty (String property, NodeHandler handler) {
        final Object value = properties.get (property);

        if(value instanceof Map) {
            handler.handle (getChildNode (property));
        } else if (value instanceof Collection) {
            for (Node node : getChildNodes (property)) {
                handler.handle (node);
            }
        }
    }

    /**
     * converts the array value of the given property key to a collection of {@link Node}s. If the
     * elements are not maps the result collection will be empty.
     *
     * @param key property name
     * @return collection of {@link Node}s
     */
    @SuppressWarnings ("unchecked")
    // todo check collection, else throw
    public Collection<Node> getChildNodes (String key) {
        if (!properties.containsKey (key))
            return Collections.EMPTY_LIST;

        final Collection<Node> nodes = new ArrayList<> ();
        final Collection<?> values = (Collection<?>) properties.get (key);
        for (Object value : values) {
            if (value instanceof Map) {
                nodes.add (new Node ((Map<String, Object>) value));
            }
        }
        return nodes;
    }

    /**
     * converts the value of the given property key to a {@code T} using the given factory to
     * convert the value to {@code T}.
     *
     * @param key property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> @Nullable T getChildAs (String key, NodeConverter<T> factory) {
        final Node childNode = getChildNode (key);
        if (childNode == null)
            return null;

        return factory.create (childNode);
    }

    /**
     * same as {@link #getChildAs}, but throws if the property values is {@code null}.
     */
    public <T> @Nullable T getRequiredChild (String key, NodeConverter<T> factory) {
        return notNullProperty (key, getChildAs (key, factory));
    }

    /**
     * converts the value of the given property name to a collection of {@code T}s using the given
     * factory to convert all property values to {@code T}s.
     *
     * @param key property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return {@code T}
     */
    public <T> Collection<T> getChildArrayAs (String key, NodeConverter<T> factory) {
        return getChildNodes (key)
            .stream ()
            .map (factory::create)
            .collect(Collectors.toList());
    }

    /**
     * converts the value of the given property name to a map from property name to {@code T} using
     * the given factory to convert all property values to {@code T}.
     *
     * @param key property name
     * @param factory converter from {@link Node} to {@code T}
     * @param <T> type of the target OpenAPI model object
     * @return map of property values to {@code T}s
     */
    @SuppressWarnings ("unchecked")
    public <T> @Nullable Map<String, T> getChildMapAs (String key, NodeConverter<T> factory) {
        Map<String, Object> value = (Map<String, Object>) properties.get (key);
        if (value == null)
            return null;

        return new Node(value).getMapAs (factory);
    }

    /**
     * makes sure that the property value is not null. Throws if the property value is null.
     *
     * @param property property name
     * @param value property value
     * @param <T>type of value
     * @return property value
     */
    private <T> T notNullProperty (String property, @Nullable T value) {
        if (value == null) {
            throw new NullPropertyException(String.format ("property %s should not be null", property));
        }
        return value;
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
    public int getCountProperties () {
        return properties.size ();
    }
}
