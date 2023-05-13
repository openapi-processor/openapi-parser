/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class ValidationError {
    private final String instanceLocation;
    private final String keywordLocation;
    private final String keywordLocationAbsolute;

    private final String text;

    public ValidationError (
        String instanceLocation,
        String keywordLocation,
        String keywordLocationAbsolute,
        String text
    ) {
        this.instanceLocation = instanceLocation;
        this.keywordLocation = keywordLocation;
        this.keywordLocationAbsolute = keywordLocationAbsolute;
        this.text = text;
    }

    public String getInstanceLocation () {
        return instanceLocation;
    }

    public String getKeywordLocation () {
        return keywordLocation;
    }

    public String getKeywordLocationAbsolute () {
        return keywordLocationAbsolute;
    }

    public String getText () {
        return text;
    }
}
