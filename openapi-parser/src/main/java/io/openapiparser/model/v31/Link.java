/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.converter.ExtensionsConverter;
import io.openapiparser.schema.Bucket;

import java.util.Map;

/**
 * the <em>Link</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#link-object">4.8.20 Link Object</a>
 */
public class Link implements Extensions {
    private final Context context;
    private final Bucket properties;

    public Link (Context context, Bucket properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return properties.convert (new ExtensionsConverter ());
    }
}
