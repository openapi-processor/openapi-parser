/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private final Map<String, Object> node;

    public Node (Map<String, Object> node) {
        this.node = node;
    }

    public @Nullable Object get (String key) {
        return node.get (key);
    }

    public @Nullable String getString (String key) {
        return (String) node.get (key);
    }

    @SuppressWarnings ("unchecked")
    public <T> Map<String, T> getMapAs (NodeConverter<T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();
        node.forEach ((k, v) -> {
            result.put (k, factory.create (new Node ((Map<String, Object>) v)));
        });
        return result;
    }

    @SuppressWarnings ("unchecked")
    public @Nullable Node getChildNode (String key) {
        if (!node.containsKey (key))
            return null;

        return new Node ((Map<String, Object>) node.get (key));
    }

    @SuppressWarnings ("unchecked")
    public Collection<Node> getChildNodes (String key) {
        if (!node.containsKey (key))
            return Collections.EMPTY_LIST;

        return ((Collection<Map<String, Object>>) node.get (key))
            .stream ()
            .map (Node::new)
            .collect(Collectors.toList());
    }

    public <T> @Nullable T getChildAs (String key, NodeConverter<T> factory) {
        final Node childNode = getChildNode (key);
        if (childNode == null)
            return null;

        return factory.create (childNode);
    }

    public <T> Collection<T> getChildArrayAs (String key, NodeConverter<T> factory) {
        return getChildNodes (key)
            .stream ()
            .map (factory::create)
            .collect(Collectors.toList());
    }

    @SuppressWarnings ("unchecked")
    public <T> @Nullable Map<String, T> getChildMapAs (String key, NodeConverter<T> factory) {
        Map<String, Object> keyValue = (Map<String, Object>) node.get (key);
        if (keyValue == null)
            return null;

        Map<String, T> result = new LinkedHashMap<> ();
        keyValue.forEach ((k, v) -> {
            result.put (k, factory.create (new Node ((Map<String, Object>) v)));
        });
        return result;
    }

    public boolean containsKey (String key) {
        return node.containsKey (key);
    }

    public int getSize() {
        return node.size ();
    }

    public Set<String> getKeys() {
        return node.keySet ();
    }
}
