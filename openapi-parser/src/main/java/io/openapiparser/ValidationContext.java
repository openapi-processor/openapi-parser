/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.Collection;

public class ValidationContext {
    private final URI source;
    private final String path;

    public ValidationContext (URI source) {
        this.source = source;
        this.path = "$";
    }

    public ValidationContext (URI source, String path) {
        this.source = source;
        this.path = path;
    }

    public String getPropertyPath(String property) {
        return path + "." + property;
    }

    public String getPropertyPath(Collection<String> properties) {
        return path + ".(" + String.join ("|", properties) + ")";
    }

}
