/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

@Deprecated
public interface Validator {

    Collection<ValidationMessage> validate(ValidationContext context, Node node);

}
