/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.result;

import io.openapiprocessor.jsonschema.validator.ValidationMessage;

public class FullResultTextBuilder implements ResultTextBuilder {

    @Override
    public String getText (ValidationMessage message) {
        String location = trim(lastPartOfPath(message.getInstancePath ()), 20);

        String instancePath = message.getInstancePath ().length () != 0
            ? trim (message.getInstancePath (), 40)
            : "/";

        String text = trim (message.getText (), 60);

        String schemaPath = message.getSchemaPath () != null
            ? message.getSchemaPath ()
            : message.getSchemaScope ();

        return String.format("%-20s: %-40s - %-60s   schema: %s",
            location,
            instancePath,
            text,
            schemaPath
        );
    }

    private String lastPartOfPath(String path) {
        int index = path.lastIndexOf ("/");
        if (index == -1)
            return path;

        return path.substring (index);
    }

    private String trim(String source, int maxLength) {
        if (source.length () <= maxLength) {
            return source;
        }

        int trimmedStart = source.length() - (maxLength - 3);
        String trimmed = source.substring(trimmedStart);
        return ".. " + trimmed;
    }
}
