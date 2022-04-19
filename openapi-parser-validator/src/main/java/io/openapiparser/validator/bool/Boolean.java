/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.bool;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

/**
 * validates boolean.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-01#section-4.4">
 *     Draft 6: JSON schema documents
 * </a>
 */
public class Boolean {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        BooleanStep step =  new BooleanStep (schema, instance);

        boolean value = schema.getBoolean ();
        if (!value) {
            step.setInvalid ();
        }
        return step;
    }

}
