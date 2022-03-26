/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public class RefValue {
    private final URI scope;
    private final Object value;

    public RefValue (URI scope, Object value) {
        this.scope = scope;
        this.value = value;
    }

    public URI getScope () {
        return scope;
    }

    public Object getValue () {
        return value;
    }
}
