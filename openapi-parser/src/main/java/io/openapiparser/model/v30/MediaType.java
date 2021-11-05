/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Nullable;
import io.openapiparser.Required;

/**
 * the <em>Media Type</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#media-type-object">4.7.14 Media Type Object</a>
 */
public class MediaType implements Reference, Extensions {

    @Override
    public boolean isRef () {
        return false;
    }

    @Override
    @Required
    public String getRef () {
        return null;
    }
}
