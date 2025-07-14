/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * validates minItems. Since Draft 4.
 */
public class MinItems {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Integer minItems = schema.getMinItems ();
        if (minItems == null)
            return;

        MinItemsStep step = new MinItemsStep (schema, instance);

        Collection<@Nullable Object> instanceValue = getInstanceValue (instance);
        if (instanceValue.size () < minItems) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private Collection<@Nullable Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
