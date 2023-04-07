/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.memory;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.*;

/**
 * Simple in-memory protocol for "loading" content from memory via URL. JDK-8 version.
 */
public class MemoryUrlStreamHandlerFactory implements URLStreamHandlerFactory {

    public static void register () {
        URL.setURLStreamHandlerFactory (new MemoryUrlStreamHandlerFactory ());
    }

    @Override
    public @Nullable URLStreamHandler createURLStreamHandler (String protocol) {
        if (!"memory".equals (protocol))
            return null;

        return new MemoryUrlStreamHandler ();
    }

}
