/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiprocessor.jsonschema.schema.Scope;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * converts the property {@link Map} {@code value} to a {@link Bucket}.
 */
public class BucketConverter implements PropertyConverter<Bucket> {
    private final Bucket parent;

    public BucketConverter (Bucket parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Bucket convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        Map<String, Object> props = Types.asMap (value);
        Scope scope = parent.getScope ().move (props);
        return new Bucket (scope, location, props);
    }
}
