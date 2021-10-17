/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Map;
import java.util.Set;

/**
 * the <em>Paths</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#paths-object">4.7.8 Paths Object</a>
 */
public class Paths implements Extensions {
    private final Context context;
    private final Node node;

    public Paths (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public Map<String, PathItem> getPathItems() {
        return node.getMapAs (node -> new PathItem (context, node));
    }

    public PathItem getPathItem(String path) {
        return node.getChildAs (path, node -> new PathItem (context, node));
    }

    public Set<String> pathSet() {
        return node.getKeys ();
    }

}
