/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.util.EnumSet;

import static io.openapiparser.schema.KeywordType.*;

public class Keyword {
    private final EnumSet<KeywordType> types;

    public Keyword () {
        types = EnumSet.of (KeywordType.NONE);
    }

    public Keyword (EnumSet<KeywordType> types) {
        this.types = types;
    }

    public boolean isSchema () {
        return types.contains (SCHEMA);
    }

    public boolean isSchemaArray () {
        return types.contains (SCHEMA_ARRAY);
    }

    public boolean isSchemaMap () {
        return types.contains (SCHEMA_MAP);
    }

    public boolean isNavigatable () {
        return types.contains (SCHEMA)
            || types.contains (SCHEMA_MAP)
            || types.contains (SCHEMA_ARRAY);
    }
}
