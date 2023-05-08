/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */
package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.Bucket;

public class Factory<T> {
    private final Context context;
    private final Class<T> clazz;

    public Factory (Context context, Class<T> clazz) {
        this.context = context;
        this.clazz = clazz;
    }

    public T create (Bucket target) {
        try {
            return clazz
                .getDeclaredConstructor (Context.class, Bucket.class)
                .newInstance (context.withScope (target.getScope ()), target);
        } catch (Exception e) {
            throw new RuntimeException (String.format("failed to create %s", clazz.getName ()), e);
        }
    }
}
