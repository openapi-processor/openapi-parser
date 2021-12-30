/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

public class ValidationMessage {
    // kind/source ?
    // warn/error ?
    // file ?
    private final String path;
    private final String text;

    public ValidationMessage (String path, String text) {
        this.path = path;
        this.text = text;
    }

    public String getPath () {
        return path;
    }

    public String getText () {
        return text;
    }

    @Override
    public String toString () {
        return String.format ("%s: %s", path, text);
    }
}
