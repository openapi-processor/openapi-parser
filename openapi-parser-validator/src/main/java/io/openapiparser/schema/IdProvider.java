/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.as;
import static io.openapiparser.converter.Types.isString;

/**
 * find (draft4) scope in given object properties.
 */
public class IdProvider {

    /**
     * get the scope from object properties if available.
     *
     * @param properties the object properties
     * @return scope id or null
     */
    public @Nullable String getId (Map<String, Object> properties) {
        Object id = properties.get ("id");
        Object ref = properties.get ("$ref");

        if (isString (ref) || !isString (id))
            return null;

        return as (id);
    }
}
