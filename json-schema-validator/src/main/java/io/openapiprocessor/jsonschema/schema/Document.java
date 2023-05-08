/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

class Document {
    enum Source {
        ID, ANCHOR, DYNAMIC_ANCHOR
    }

    private final Source source;
    private final Object document;

    public Document (Object document, Source source) {
        this.source = source;
        this.document = document;
    }

    public Source getSource () {
        return source;
    }

    public Object getDocument () {
        return document;
    }
}
