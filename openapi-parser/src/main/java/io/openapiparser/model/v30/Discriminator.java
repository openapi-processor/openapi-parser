/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.converter.MapObjectsOrEmptyFromPropertyConverter;
import io.openapiparser.converter.StringConverterRequired;
import io.openapiparser.schema.PropertyBucket;

import java.util.Map;

import static io.openapiparser.Keywords.MAPPING;
import static io.openapiparser.Keywords.PROPERTY_NAME;

/**
 * the <em>Discriminator</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#discriminator-object">4.7.25 Discriminator Object</a>
 */
public class Discriminator {
    private final Context context;
    private final PropertyBucket properties;

    public Discriminator (Context context, PropertyBucket properties) {
        this.context = context;
        this.properties = properties;
    }

    public String getPropertyName () {
        return getStringOrThrow (PROPERTY_NAME);
    }

    public Map<String, String> getMapping () {
        return getMapStringsOrEmpty (MAPPING);
    }

    private String getStringOrThrow (String property) {
        return properties.convert (property, new StringConverterRequired ());
    }

    private Map<String, String> getMapStringsOrEmpty (String property) {
        return properties.convert (property, new MapObjectsOrEmptyFromPropertyConverter<> (context, String.class));
    }
}
