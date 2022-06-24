/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.schema.ScopeSupport.updateScope;

/**
 * converts the property {@link Map} {@code value} to a {@link Bucket}.
 */
public class BucketConverter implements PropertyConverter<Bucket> {
    private final Bucket parent;
    private final SchemaVersion version;

    public BucketConverter (Bucket parent, SchemaVersion version) {
        this.parent = parent;
        this.version = version;
    }

    @Override
    public @Nullable Bucket convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        Map<String, Object> props = asMap (value);
        URI scope = updateScope (props, parent.getScope (), version.getIdProvider ());
        return new Bucket (scope, location, props);
    }
}
