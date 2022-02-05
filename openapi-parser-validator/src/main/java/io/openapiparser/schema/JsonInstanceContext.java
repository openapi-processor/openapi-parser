/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public class JsonInstanceContext {
    private final URI baseUri;
    private final ReferenceRegistry references;

    public JsonInstanceContext (URI baseUri, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.references = references;
    }
}
