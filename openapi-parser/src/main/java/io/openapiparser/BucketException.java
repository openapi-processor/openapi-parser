/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class BucketException extends RuntimeException {

    public BucketException(URI documentUri) {
        super(String.format("failed to create Bucket from OpenAPI document %s", documentUri));
    }
}
