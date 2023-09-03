/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Types.asString;
import static io.openapiprocessor.jsonschema.support.Types.isString;

/**
 * draft-2019-09 IdProvider
 */
public class IdProvider201909 implements IdProvider {
    @Override
    public @Nullable String getId (Map<String, Object> properties) {
        Object rawId = properties.get (Keywords.ID);
        if (!isString (rawId))
            return null;

        String id = asString (rawId);

        // id must not contain fragment, except an empty fragment
        if (id.contains (Keywords.HASH) && !id.endsWith (Keywords.HASH)) {
            return null;
        }

        return id;
    }
}
