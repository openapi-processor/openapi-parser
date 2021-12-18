/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Map;

/**
 * the <em>Encoding</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#example-object">4.8.15 Encoding Object</a>
 */
public class Encoding implements Extensions {
    private final Context context;
    private final Node node;

    public Encoding (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }
}
