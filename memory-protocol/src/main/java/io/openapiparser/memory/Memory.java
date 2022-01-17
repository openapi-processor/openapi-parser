/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.memory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * in-memory content by path.
 */
public class Memory {
    private static final ConcurrentHashMap<String, String> contents = new ConcurrentHashMap<> ();

    public static String get(String path) {
        if(!contents.containsKey (path))
            throw new RuntimeException ();

        return contents.get (path);
    }

    public static void add(String path, String data) {
        contents.put (path, data);
    }

    public static void clear() {
        contents.clear ();
    }
}
