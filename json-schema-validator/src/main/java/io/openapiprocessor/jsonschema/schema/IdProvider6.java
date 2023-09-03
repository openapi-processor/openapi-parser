/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * draft-06 IdProvider
 */
public class IdProvider6 implements IdProvider {
    @Override
    public @Nullable String getId (Map<String, Object> properties) {
        Object ref = properties.get (Keywords.REF);
        if (Types.isString (ref))
            return null;

        Object id = properties.get (Keywords.ID);
        if (!Types.isString (id))
            return null;

        return Types.asString (id);
    }
}
