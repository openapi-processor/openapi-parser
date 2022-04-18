/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UriSupport {

    public static URI createUri (String source) {
        return URI.create (encodePath (source));
    }

    public static String encodePath (String source) {
        return source
            .replace ("{", "%7B")
            .replace ("}", "%7D");
    }

    public static String decodePath (String source) {
        return source
            .replace ("%7B", "{")
            .replace ("%7D", "}");
    }

    private static String encode (String source) {
        try {
            return URLEncoder.encode (source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException (ex);
        }
    }

    private static String decode (String source) {
        try {
            return URLDecoder.decode (source, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException (ex);
        }
    }
}
