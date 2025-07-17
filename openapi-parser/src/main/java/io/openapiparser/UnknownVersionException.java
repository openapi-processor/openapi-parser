/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import org.checkerframework.checker.nullness.qual.Nullable;

public class UnknownVersionException extends RuntimeException {
    public UnknownVersionException (@Nullable String version) {
        super(version != null ? version : "null");
    }
}
