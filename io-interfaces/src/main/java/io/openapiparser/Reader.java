/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * reader to load a document by its URI/URL. Should handle security if required.
 */
public interface Reader {

    /**
     * load document.
     *
     * @param uri uri of the document.
     * @return input stream of document.
     * @throws IOException if reading fails.
     */
    InputStream read(URI uri) throws IOException;
}
