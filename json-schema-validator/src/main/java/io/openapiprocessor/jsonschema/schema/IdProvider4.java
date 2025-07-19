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
 * draft-04 IdProvider
 */
public class IdProvider4 implements IdProvider {
    @Override
    public @Nullable String getId (Map<String, @Nullable Object> properties) {
        Object ref = properties.get (Keywords.REF);
        if (isString (ref))
            return null;

        Object id = properties.get (Keywords.ID4);
        if (!isString (id))
            return null;

        return asString (id);
    }
}
