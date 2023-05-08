/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class JsonPointerSupport {

    public static String encode (String source) {
        return source
            .replace ("~", "~0")
            .replace ("/", "~1");
    }

    public static String decode (String source) {
        return source
            .replace ("~1", "/")
            .replace ("~0", "~");
    }

    public static String encodePath (String source) {
        return source
            .replace ("{", "%7B")
            .replace ("}", "%7D");
    }

}
