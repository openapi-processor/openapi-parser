/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Request Body</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#request-body-object">4.7.13 Request Body Object</a>
 */
public class RequestBody implements Extensions {
    private final Context context;
    private final Node node;

    public RequestBody (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public @Nullable String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    public Map<String, MediaType> getContent () {
        return node.getMapObjectValuesOrEmpty (CONTENT, node -> new MediaType (context, node));
    }

    public Boolean getRequired () {
        return node.getBooleanValue (REQUIRED, false);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
