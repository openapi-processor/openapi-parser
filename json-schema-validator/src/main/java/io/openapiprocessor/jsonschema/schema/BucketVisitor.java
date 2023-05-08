/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

/**
 * represents a function that processes source {@link Bucket}.
 */
@FunctionalInterface
public interface BucketVisitor {

    /**
     * the function applied to {@link Bucket}.
     *
     * @param properties the source {@link Bucket}
     */
    void visit (Bucket properties);
}
