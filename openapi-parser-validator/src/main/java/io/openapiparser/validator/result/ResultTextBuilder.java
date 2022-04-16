/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.result;

import io.openapiparser.validator.ValidationMessage;

public interface ResultTextBuilder {
    String getText (ValidationMessage message);
}
