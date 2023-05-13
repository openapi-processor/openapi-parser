/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class ValidationErrorTextBuilder {

    public String getText (ValidationError error) {
        String keywordLocation = trim(error.getKeywordLocationAbsolute (), 60);
        String instanceLocation = error.getInstanceLocation ().length () != 0
            ? trim (error.getInstanceLocation (), 40)
            : "/";

        String text = trim (error.getText (), 40);

        return String.format("%-30s: %-40s - %-60s",
            instanceLocation,
            text,
            keywordLocation
        );
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
