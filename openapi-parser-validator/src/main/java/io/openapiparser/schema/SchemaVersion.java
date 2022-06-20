/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public enum SchemaVersion {
    Default ("http://json-schema.org/draft-07/schema#",
        SchemaKeywords.draft7, IdProvider.DRAFT7, false),
    Draft201909 ("https://json-schema.org/draft/2019-09/schema",
        SchemaKeywords.draft201909, IdProvider.DRAFT201909, true),
    Draft7 ("http://json-schema.org/draft-07/schema#",
        SchemaKeywords.draft7, IdProvider.DRAFT7, false),
    Draft6 ("http://json-schema.org/draft-06/schema#",
        SchemaKeywords.draft6, IdProvider.DRAFT6, false),
    Draft4 ("http://json-schema.org/draft-04/schema#",
        SchemaKeywords.draft4, IdProvider.DRAFT4, false);

    private final URI schema;
    private final SchemaKeywords keywords;
    private final IdProvider idProvider;
    private final boolean allowsRefSiblings;

    SchemaVersion(
        String schema,
        SchemaKeywords keywords,
        IdProvider idProvider,
        boolean allowsRefSiblings
    ) {
        this.schema = URI.create (schema);
        this.keywords = keywords;
        this.idProvider = idProvider;
        this.allowsRefSiblings = allowsRefSiblings;
    }

    public URI getSchema () {
        return schema;
    }

    public @Nullable Keyword getKeyword (String name) {
        return keywords.getKeyword (name);
    }

    public IdProvider getIdProvider () {
        return idProvider;
    }

    public boolean allowsRefSiblings () {
        return allowsRefSiblings;
    }
}
