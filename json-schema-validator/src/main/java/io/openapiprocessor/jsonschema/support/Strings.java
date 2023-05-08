/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Strings {
    private static final int EOF = -1;

    public static String of(InputStream source) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Reader reader = new BufferedReader (new InputStreamReader (source, StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != EOF) {
                content.append((char) c);
            }
        }
        return content.toString ();
    }

    public static boolean isEmpty(String source) {
        return source == null || source.isEmpty ();
    }

}
