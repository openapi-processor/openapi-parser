/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import java.util.Collection;

public class Annotation {
    private final String keyword;
    private final Object value;

    public Annotation (String keyword, Object value) {
        this.keyword = keyword;
        this.value = value;
    }

    public String getKeyword () {
        return keyword;
    }

    public Object getValue () {
        return value;
    }

    @SuppressWarnings ("unchecked")
    public Collection<String> asStrings() {
        return (Collection<String>) value;
    }
}
