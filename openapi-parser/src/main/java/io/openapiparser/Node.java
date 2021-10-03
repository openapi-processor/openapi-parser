package io.openapiparser;

import java.util.*;
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
