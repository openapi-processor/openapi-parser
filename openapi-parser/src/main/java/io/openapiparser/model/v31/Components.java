/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Components</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#components-object">4.8.7 Components Object</a>
 */
public class Components extends Properties implements Extensions {

    public Components (Context context, Bucket bucket) {
        super (context, bucket);
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
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
