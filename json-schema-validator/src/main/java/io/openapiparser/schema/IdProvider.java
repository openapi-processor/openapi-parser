/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public interface IdProvider {
    IdProvider DRAFT201909 = new IdProvider201909 ();
    IdProvider DRAFT7 = new IdProvider6 ();
    IdProvider DRAFT6 = new IdProvider6 ();
    IdProvider DRAFT4 = new IdProvider4 ();

    /**
     * get the scope from an object if available.
     *
     * @param properties the object properties
     * @return scope id or null
     */
    @Nullable String getId (Map<String, Object> properties);
}
