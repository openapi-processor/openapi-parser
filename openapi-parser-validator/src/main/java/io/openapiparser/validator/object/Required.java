/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates required. Since Draft 4.
 */
public class Required {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        Collection<String> requiredProperties = schema.getRequired ();

        if (requiredProperties == null)
            return;

        RequiredStep step = new RequiredStep (schema, instance);
        requiredProperties.forEach (p -> {
            boolean found = instanceProperties.contains (p);
            if (found) {
                return;
            }

            RequireStep rStep = new RequireStep (schema, instance, p);
            rStep.setInvalid ();
            step.add (rStep);
        });

        parentStep.add (step);
    }
}
