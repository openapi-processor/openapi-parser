/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asString;

/**
 * draft-2019-09 IdProvider
 */
public class IdProvider201909 implements IdProvider {
    @Override
    public @Nullable String getId (Map<String, Object> properties) {
        String id = asString (properties.get (Keywords.ID));
        if (id == null)
            return null;

        // id must not contain fragment, except an empty fragment
        if (id.contains (Keywords.HASH) && !id.endsWith (Keywords.HASH)) {
            return null;
        }

        return id;
    }
}
