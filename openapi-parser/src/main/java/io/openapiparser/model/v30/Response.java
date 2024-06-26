/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiparser.support.Required;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Response</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#response-object">4.7.17 Response Object</a>
 */
public class Response extends Properties implements Reference, Extensions {

    public Response (Context context, Bucket bucket) {
        super (context, bucket);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    /** {@inheritDoc} */
    @Required
    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    public Response getRefObject () {
        return getRefObjectOrThrow (Response.class);
    }

    @Required
    public String getDescription () {
        return getStringOrThrow (DESCRIPTION);
    }

    public Map<String, Header> getHeaders () {
        return getMapObjectsOrEmpty (HEADERS, Header.class);
    }

    public Map<String, MediaType> getContent () {
        return getMapObjectsOrEmpty (CONTENT, MediaType.class);
    }

    public Map<String, Link> getLinks () {
        return getMapObjectsOrEmpty (LINKS, Link.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
