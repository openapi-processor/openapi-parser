/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.DEFAULT;

/**
 * the <em>Responses</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#responses-object">4.7.16 Responses Object</a>
 */
public class Responses extends Properties implements Extensions {

    public Responses (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public Map<String, Response> getResponses () {
        return getMapObjectsOrEmpty (Response.class);
    }

    public @Nullable Response getResponse (String httpStatus) {
        return getObjectOrNull (httpStatus, Response.class);
    }

    public @Nullable Response getDefault () {
        return getObjectOrNull (DEFAULT, Response.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
