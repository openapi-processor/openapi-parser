/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * converts property {@code value} to {@link PropertyBucket} object.
 */
public class PropertyBucketConverter implements PropertyConverter<PropertyBucket> {

    @Override
    public @Nullable PropertyBucket convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        //noinspection unchecked
        return new PropertyBucket (JsonPointer.fromJsonPointer (location), (Map< String, Object >) value);
    }
}
