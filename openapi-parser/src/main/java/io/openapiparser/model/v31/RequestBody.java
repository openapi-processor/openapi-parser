/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Request Body</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#request-body-object">4.8.13 Request Body Object</a>
 */
public class RequestBody extends Properties implements Reference, Extensions {

    public RequestBody (Context context, Bucket bucket) {
        super (context, bucket);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    /** {@inheritDoc} */
    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    @Override
    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    public RequestBody getRefObject () {
        return getRefObjectOrThrow (RequestBody.class);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public Map<String, MediaType> getContent () {
        return getMapObjectsOrEmpty (CONTENT, MediaType.class);
    }

    public Boolean getRequired () {
        return getBooleanOrDefault (REQUIRED, false);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
