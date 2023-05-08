/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.result;

import io.openapiprocessor.jsonschema.validator.ValidationMessage;

public interface ResultTextBuilder {
    String getText (ValidationMessage message);
}
