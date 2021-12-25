/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Components</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#components-object">4.7.7 Components Object</a>
 */
public class Components implements Extensions {
    private final Context context;
    private final Node node;

    public Components (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public Map<String, Schema> getSchemas () {
        return node.getMapObjectValuesOrEmpty (SCHEMAS, node -> new Schema (context, node));
    }

    public Map<String, Response> getResponses () {
        return node.getMapObjectValuesOrEmpty (RESPONSES, node -> new Response (context, node));
    }

    public Map<String, Parameter> getParameters () {
        return node.getMapObjectValuesOrEmpty (PARAMETERS, node -> new Parameter (context, node));
    }

    public Map<String, Example> getExamples () {
        return node.getMapObjectValuesOrEmpty (EXAMPLES, node -> new Example (context, node));
    }

    public Map<String, RequestBody> getRequestBodies () {
        return node.getMapObjectValuesOrEmpty (REQUEST_BODIES, node -> new RequestBody (context, node));
    }

    public Map<String, Header> getHeaders () {
        return node.getMapObjectValuesOrEmpty (HEADERS, node -> new Header (context, node));
    }

    public Map<String, SecurityScheme> getSecuritySchemes () {
        return node.getMapObjectValuesOrEmpty (SECURITY_SCHEMES, node -> new SecurityScheme (context, node));
    }

    public Map<String, Link> getLinks () {
        return node.getMapObjectValuesOrEmpty (LINKS, node -> new Link (context, node));
    }

    public Map<String, Callback> getCallbacks () {
        return node.getMapObjectValuesOrEmpty (CALLBACKS, node -> new Callback (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
