/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class NodePathFinder {
    private static final String SLASH =  "/";

    private final Node root;

    NodePathFinder (Node root) {
        this.root = root;
    }

    @Nullable Object find(String path) {
        if (!isPath (path)) {
            return root.get (path);
        } else {
            String[] parts = getParts (path);
            int last = parts.length - 1;

            Node node = root;
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];

                if (node == null || !node.containsKey (part))
                    return null;

                if (i == last)
                    return node.get (part);

                node = node.getChildNode (part);
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
}
