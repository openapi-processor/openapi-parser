/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Strings {
    private static final int EOF = -1;
    private static final Charset CHARSET_UTF8 = Charset.forName (StandardCharsets.UTF_8.name ());

    public static String of(InputStream source) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Reader reader = new BufferedReader (new InputStreamReader (source, CHARSET_UTF8))) {
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
