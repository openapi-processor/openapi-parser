/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class Keyword {
    private final String keyword;
    private final boolean required;

    public Keyword (String keyword) {
        this.keyword = keyword;
        this.required = false;
    }

    public Keyword (String keyword, boolean required) {
        this.keyword = keyword;
        this.required = required;
    }

    public String getKeyword () {
        return keyword;
    }

    public boolean isRequired () {
        return required;
    }
}
