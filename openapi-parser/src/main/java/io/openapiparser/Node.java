package io.openapiparser;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Node {
    private final Map<String, Object> node;

    public Node (Map<String, Object> node) {
        this.node = node;
    }

    public Object get (String key) {
        return node.get (key);
    }

    public String getString (String key) {
        return (String) node.get (key);
    }

    @SuppressWarnings ("unchecked")
    public <T> Map<String, T> getMapAs (Function<Node, T> factory) {
        Map<String, T> result = new LinkedHashMap<> ();
        node.forEach ((k, v) -> {
            result.put (k, factory.apply (new Node ((Map<String, Object>) v)));
        });
        return result;
    }

    @SuppressWarnings ("unchecked")
    public Node getChildNode (String key) {
        return new Node ((Map<String, Object>) node.get (key));
    }

    @SuppressWarnings ("unchecked")
    public Collection<Node> getChildNodes (String key) {
        return ((Collection<Map<String, Object>>) node.get (key))
            .stream ()
            .map (Node::new)
            .collect(Collectors.toList());
    }

    public <T> T getChildAs (String key, Function<Node, T> factory) {
        return factory.apply (getChildNode (key));
    }

    public <T> Collection<T> getChildArrayAs (String key, Function<Node, T> factory) {
        return getChildNodes (key)
            .stream ()
            .map (factory)
            .collect(Collectors.toList());
    }

    @SuppressWarnings ("unchecked")
    public <T> Map<String, T> getChildMapAs (String key, Function<Node, T> factory) {
        Map<String, Object> keyValue = (Map<String, Object>) node.get (key);

        Map<String, T> result = new LinkedHashMap<> ();
        keyValue.forEach ((k, v) -> {
            result.put (k, factory.apply (new Node ((Map<String, Object>) v)));
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
