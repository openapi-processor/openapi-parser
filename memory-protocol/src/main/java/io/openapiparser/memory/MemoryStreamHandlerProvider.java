/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.memory;

import java.net.URLStreamHandler;
//import java.net.spi.URLStreamHandlerProvider;

/**
 * Simple in-memory protocol for "loading" content from memory via URL.
 *
 * JDK-9+
 */
public class MemoryStreamHandlerProvider /*extends URLStreamHandlerProvider*/ {

    //@Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
        if (!"memory".equals (protocol))
            return null;

        return new MemoryUrlStreamHandler ();
    }
}
