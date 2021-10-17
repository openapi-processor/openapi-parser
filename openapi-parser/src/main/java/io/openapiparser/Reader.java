/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * reader to load the OpenAPI description (base and $ref's). Should handle security if required.
 */
public interface Reader {

    /**
     * load OpenAPI description.
     *
     * @param uri uri of the OpenAPI description.
     * @return stream of the description.
     */

    InputStream read(URI uri) throws IOException;

}
