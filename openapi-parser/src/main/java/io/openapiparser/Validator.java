/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Collection;

public interface Validator {

    Collection<ValidationMessage> validate(ValidationContext context, Node node);

}
