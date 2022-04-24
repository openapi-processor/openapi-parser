/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public enum SchemaVersion {
    Default ("", IdProvider.LATEST),
    Draft6("http://json-schema.org/draft-06/schema#", IdProvider.LATEST),
    Draft4("http://json-schema.org/draft-04/schema#", IdProvider.DRAFT4);

    private final URI schema;
    private final IdProvider idProvider;

    SchemaVersion(String schema, IdProvider idProvider) {
        this.schema = URI.create (schema);
        this.idProvider = idProvider;
    }

    public URI getSchema () {
        return schema;
    }

    public IdProvider getIdProvider () {
        return idProvider;
    }
}



