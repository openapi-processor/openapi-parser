/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class UnknownVersionException extends RuntimeException {
    public UnknownVersionException (String version) {
        super(version);
    }
}
