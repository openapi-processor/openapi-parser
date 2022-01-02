/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.schema.JsonPointer.fromJsonPointer;

/**
 * converts the property {@link Map} {@code value} to a {@link Bucket}.
 */
public class BucketConverter implements PropertyConverter<Bucket> {

    @Override
    public @Nullable Bucket convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        return new Bucket (fromJsonPointer (location), asMap (value));
    }
}
