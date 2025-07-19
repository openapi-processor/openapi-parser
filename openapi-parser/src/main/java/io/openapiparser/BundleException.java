/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class BundleException extends RuntimeException {

    public BundleException(URI documentUri) {
        super(String.format("failed find OpenAPI document %s", documentUri));
    }
}
