/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asMap;

/**
 * converts the property {@link Map} {@code value} to a {@link Bucket}.
 */
public class BucketConverter implements PropertyConverter<Bucket> {
    private final Bucket parent;

    public BucketConverter (Bucket parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Bucket convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        return new Bucket (parent.getSource (), location, asMap (value));
    }
}
