/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;

/**
 * validates minItems.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.12">
 *     minItems</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.3">
 *     minItems</a>
 */
public class MinItems {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        MinItemsStep step = new MinItemsStep (schema, instance);

        Collection<Object> instanceValue = instance.asCollection ();
        int minItems = schema.getMinItems ();
        if (instanceValue.size () < minItems) {
            step.setInvalid ();
            return step;
        }

        return step;
    }
}
