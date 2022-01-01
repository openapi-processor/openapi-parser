/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.Node;
import io.openapiparser.converter.ObjectMapPropertyConverter;
import io.openapiparser.schema.PropertyBucket;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Components</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#components-object">4.8.7 Components Object</a>
 */
public class Components implements Extensions {
    private final Context context;
    private final Node node;
    private final PropertyBucket properties;

    public Components (Context context, Node node) {
        this.context = context;
        this.node = node;
        this.properties = node.toBucket ();
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
        return getObjectMapFromProperty (HEADERS, Header.class);
    }

    public Map<String, SecurityScheme> getSecuritySchemes () {
        return getObjectMapFromProperty (SECURITY_SCHEMES, SecurityScheme.class);
    }

    public Map<String, Link> getLinks () {
        return getObjectMapFromProperty (LINKS, Link.class);
    }

    public Map<String, Callback> getCallbacks () {
        return getObjectMapFromProperty (CALLBACKS, Callback.class);
    }

    public Map<String, PathItem> getPathItems () {
        return node.getMapObjectValuesOrEmpty (PATH_ITEMS, node -> new PathItem (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private <T> Map<String, T> getObjectMapFromProperty (String property, Class<T> clazz) {
        return properties.convert (property, new ObjectMapPropertyConverter<> (context, clazz));
    }
}
