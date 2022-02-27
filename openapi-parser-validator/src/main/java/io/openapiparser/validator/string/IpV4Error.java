/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link IpV4}.
 */
public class IpV4Error extends ValidationMessage {
    public IpV4Error (String path) {
        super (path, "should conform to rfc2673");
    }
}
