/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.converter.ExtensionsConverter;
import io.openapiparser.converter.MapObjectsOrEmptyFromPropertyConverter;
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
    private final PropertyBucket properties;

    public Components (Context context, PropertyBucket properties) {
        this.context = context;
        this.properties = properties;
    }

    public Map<String, Schema> getSchemas () {
        return getMapObjectsOrEmpty (SCHEMAS, Schema.class);
    }

    public Map<String, Response> getResponses () {
        return getMapObjectsOrEmpty (RESPONSES, Response.class);
    }

    public Map<String, Parameter> getParameters () {
        return getMapObjectsOrEmpty (PARAMETERS, Parameter.class);
    }

    public Map<String, Example> getExamples () {
        return getMapObjectsOrEmpty (EXAMPLES, Example.class);
    }

    public Map<String, RequestBody> getRequestBodies () {
        return getMapObjectsOrEmpty (REQUEST_BODIES, RequestBody.class);
    }

    public Map<String, Header> getHeaders () {
        return getMapObjectsOrEmpty (HEADERS, Header.class);
    }

    public Map<String, SecurityScheme> getSecuritySchemes () {
        return getMapObjectsOrEmpty (SECURITY_SCHEMES, SecurityScheme.class);
    }

    public Map<String, Link> getLinks () {
        return getMapObjectsOrEmpty (LINKS, Link.class);
    }

    public Map<String, Callback> getCallbacks () {
        return getMapObjectsOrEmpty (CALLBACKS, Callback.class);
    }

    public Map<String, PathItem> getPathItems () {
        return getMapObjectsOrEmpty (PATH_ITEMS, PathItem.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return properties.convert (new ExtensionsConverter ());
    }

    private <T> Map<String, T> getMapObjectsOrEmpty (String property, Class<T> clazz) {
        return properties.convert (property, new MapObjectsOrEmptyFromPropertyConverter<> (context, clazz));
    }
}
