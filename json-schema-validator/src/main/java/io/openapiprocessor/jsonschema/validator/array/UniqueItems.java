/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * validate uniqueItems. Since Draft 4.
 */
public class UniqueItems {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Boolean uniqueItems = schema.isUniqueItems ();
        if (uniqueItems == null)
            return;

        UniqueItemsStep step = new UniqueItemsStep (schema, instance);

        if (uniqueItems) {
            Collection<Object> instanceValue = getInstanceValue (instance);
            Set<Object> items = new HashSet<> ();
            for (Object item : instanceValue) {
                if (!items.add (item)) {
                    step.setInvalid ();
                    break;
                }
            }
        }

        parentStep.add (step);
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
