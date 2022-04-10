/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;

/**
 * validates maxItems.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.2">
 *     Draft 4: maxItems
 * </a>
 */
public class MaxItems {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Collection<Object> instanceValue = instance.asCollection ();
        Integer maxItems = schema.getMaxItems ();
        if (maxItems == null)
            return new NullStep ();

        MaxItemsStep step = new MaxItemsStep (schema, instance);

        if (instanceValue.size () <= maxItems)
            return step;

        step.setInvalid ();
        return step;
    }
}
