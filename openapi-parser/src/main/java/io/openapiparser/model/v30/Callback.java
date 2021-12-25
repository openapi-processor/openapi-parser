/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Map;

/**
 * the <em>Callback</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#callback-object">4.7.18 Callback Object</a>
 */
public class Callback implements Extensions {
    private final Context context;
    private final Node node;

    public Callback (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
