/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */
package io.openapiparser.memory;

import java.io.*;
import java.net.*;

/**
 * memory:// url backed by {@link Memory}.
 */
class MemoryUrlStreamHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection (URL url) throws IOException {
        if ((!"memory".equals (url.getProtocol ()))) {
            throw new IOException (
                String.format ("unsupported protocol: %s", url.getProtocol ()));
        }

        return new URLConnection (url) {
            private String data;

            @Override
            public void connect () throws IOException {
                connected = true;
                data = Memory.get (url.getPath ());
            }

            @Override
            public long getContentLengthLong () {
                return data.length ();
            }

            @Override
            public InputStream getInputStream () throws IOException {
                if (!connected)
                    connect ();

                return new ByteArrayInputStream (data.getBytes ());
            }
        };
    }
}
