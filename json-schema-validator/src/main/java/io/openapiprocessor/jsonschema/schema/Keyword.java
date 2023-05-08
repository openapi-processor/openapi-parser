/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.util.EnumSet;

import static io.openapiprocessor.jsonschema.schema.KeywordType.*;

public class Keyword {
    public static final Keyword NONE = new Keyword();

    private final EnumSet<KeywordType> types;

    public static Keyword keyword (KeywordType value, KeywordType... values) {
        return new Keyword (EnumSet.of (value, values));
    }

    private Keyword () {
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

    public boolean isNavigable () {
        return types.contains (SCHEMA)
            || types.contains (SCHEMA_MAP)
            || types.contains (SCHEMA_ARRAY);
    }
}
