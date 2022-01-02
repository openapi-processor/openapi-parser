/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;

import java.util.Map;

import static io.openapiparser.Keywords.MAPPING;
import static io.openapiparser.Keywords.PROPERTY_NAME;

/**
 * the <em>Discriminator</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#discriminator-object">4.8.25 Discriminator Object</a>
 */
public class Discriminator extends Properties implements Extensions {

    public Discriminator (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getPropertyName () {
        return getStringOrThrow (PROPERTY_NAME);
    }

    public Map<String, String> getMapping () {
        return getMapStringsOrEmpty (MAPPING);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
