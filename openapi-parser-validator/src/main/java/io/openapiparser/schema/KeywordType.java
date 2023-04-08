/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public enum KeywordType {
    NONE,
    ANY,
    NULL,
    BOOLEAN,
    OBJECT,
    ARRAY,
    NUMBER,
    INTEGER,
    STRING,
    MAP,
    URI,
    URI_REF,
    URI_TEMPLATE,
    SCHEMA,
    SCHEMA_MAP,
    SCHEMA_ARRAY
}
