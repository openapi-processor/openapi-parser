/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.converter.ExtensionsConverter;
import io.openapiparser.schema.PropertyBucket;

import java.util.Map;

/**
 * the <em>Link</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#link-object">4.7.20 Link Object</a>
 */
public class Link implements Extensions {
    private final Context context;
    private final PropertyBucket properties;

    public Link (Context context, PropertyBucket properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return properties.convert (new ExtensionsConverter ());
    }
}
