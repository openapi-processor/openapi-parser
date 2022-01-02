/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * converts property {@code value} to {@link Bucket} object.
 */
public class BucketConverter implements PropertyConverter<Bucket> {

    @Override
    public @Nullable Bucket convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        //noinspection unchecked
        return new Bucket (JsonPointer.fromJsonPointer (location), (Map< String, Object >) value);
    }
}
