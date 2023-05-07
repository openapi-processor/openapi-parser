/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.result;

public class Message {
    private final String instanceScope;
    private final String instancePath;
    private final String schemaPath;
    private final String text;

    public Message (String instanceScope, String instancePath, String schemaPath, String text) {
        this.instanceScope = instanceScope;
        this.instancePath = instancePath;
        this.schemaPath = schemaPath;
        this.text = text;
    }

    public String getInstanceScope () {
        return instanceScope;
    }

    public String getInstancePath () {
        return instancePath;
    }

    public String getSchemaPath () {
        return schemaPath;
    }

    public String getText () {
        return text;
    }

    public boolean isEmpty () {
        return text.length () == 0;
    }

    @Override
    public String toString () {
        return text;
    }
}
