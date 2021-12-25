/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.DEFAULT;

/**
 * the <em>Responses</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#responses-object">4.7.16 Responses Object</a>
 */
public class Responses implements Extensions {
    private final Context context;
    private final Node node;

    public Responses (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public Map<String, Response> getResponses () {
        return node.getMapObjectValuesOrEmpty (node -> new Response (context, node));
    }

    public @Nullable Response getResponse(String httpStatus) {
        return node.getObjectValue (httpStatus, node -> new Response (context, node));
    }

    public @Nullable Response getDefault () {
        return node.getObjectValue (DEFAULT, node -> new Response (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
