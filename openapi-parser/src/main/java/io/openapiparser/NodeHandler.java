/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * represents a function that processes a source {@link Node}.
 */
@Deprecated
@FunctionalInterface
public interface NodeHandler {

    /**
     * the function applied to a {@link Node}.
     *
     * @param node the source {@link Node}
     */
    void handle(Node node);
}
