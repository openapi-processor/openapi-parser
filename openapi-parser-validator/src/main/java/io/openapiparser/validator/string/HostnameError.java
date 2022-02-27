/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Hostname}.
 */
public class HostnameError extends ValidationMessage {
    public HostnameError (String path) {
        super (path, "should conform to rfc1034");
    }
}
