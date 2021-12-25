/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Callback</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#callback-object">4.8.18 Callback Object</a>
 */
public class Callback implements Extensions {
    private final Context context;
    private final Node node;

    public Callback (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Required
    public Map<String, PathItem> getPathItems() {
        return node.getMapObjectValuesOrEmpty (node -> new PathItem (context, node));
    }

    public @Nullable PathItem getPathItem(String path) {
        return node.getObjectValue (path, node -> new PathItem (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
