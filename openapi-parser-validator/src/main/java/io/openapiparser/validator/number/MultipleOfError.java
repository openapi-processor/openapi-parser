/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MultipleOf}.
 */
public class MultipleOfError extends ValidationMessage {
    public MultipleOfError (String path, Number multipleOf) {
        super (path, String.format ("the value should be a multiple of %s", multipleOf.toString ()));
    }
}