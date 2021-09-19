package io.openapiparser.support;

import java.util.Map;

public class Node {
    private final Map<String, Object> node;

    public Node (Map<String, Object> node) {
        this.node = node;
    }

    public String getString(String key) {
        return (String) node.get (key);
    }

    @SuppressWarnings ("unchecked")
    public Node getChildNode (String key) {
        return new Node((Map<String, Object>) node.get (key));
    }
}
