/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class UriReader implements Reader {

    @Override
    public InputStream read (URI uri) throws IOException {
        URL root = uri.toURL ();

        URLConnection connection = root.openConnection ();
        connection.connect ();

        return connection.getInputStream ();
    }

}
