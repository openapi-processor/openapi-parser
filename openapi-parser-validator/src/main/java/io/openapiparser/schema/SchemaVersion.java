/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public enum SchemaVersion {
    Default ("http://json-schema.org/draft-06/schema#",
        SchemaKeywords.draft6, IdProvider.LATEST),
    Draft6("http://json-schema.org/draft-06/schema#",
        SchemaKeywords.draft6, IdProvider.LATEST),
    Draft4("http://json-schema.org/draft-04/schema#",
        SchemaKeywords.draft4, IdProvider.DRAFT4);

    private final URI schema;
    private final IdProvider idProvider;
    private final SchemaKeywords keywords;

    SchemaVersion(String schema, SchemaKeywords keywords, IdProvider idProvider) {
        this.schema = URI.create (schema);
        this.keywords = keywords;
        this.idProvider = idProvider;
    }

    public URI getSchema () {
        return schema;
    }

    public IdProvider getIdProvider () {
        return idProvider;
    }

    public Keyword getKeyword (String name) {
        return keywords.getKeyword (name);
    }
}



