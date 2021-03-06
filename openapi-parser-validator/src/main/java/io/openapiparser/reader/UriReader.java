/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.reader;

import io.openapiparser.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Objects;

/**
 * simple {@link Reader} implementation based on {@link URL}.
 */
public class UriReader implements Reader {

    /**
     * reads the document at the given uri.
     *
     * @param uri uri of the OpenAPI document.
     * @return input stream of the uri
     * @throws IOException if it fails to read the uri
     */
    @Override
    public InputStream read (URI uri) throws IOException {
        Objects.requireNonNull (uri);

        URL root = uri.toURL ();
//        root.openStream ();
        URLConnection connection = root.openConnection ();
        connection.connect ();
        return connection.getInputStream ();
    }

}
