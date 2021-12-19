/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * represents a function that creates a model object from the source {@link Node}.
 *
 * @param <R> the result model object type
 */
@FunctionalInterface
public interface ObjectFactory<R> {
    /**
     * the function applied to a {@link Node}.
     *
     * @param node the source {@link Node}
     * @return the result
     */
    R create (Node node);
}
