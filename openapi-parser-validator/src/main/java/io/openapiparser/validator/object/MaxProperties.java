/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates maxProperties. Since Draft 4.
 */
public class MaxProperties {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        Integer maxProperties = schema.getMaxProperties ();
        if (maxProperties == null)
            return;

        MaxPropertiesStep step = new MaxPropertiesStep (schema, instance);

        if (instanceProperties.size () > maxProperties)
            step.setInvalid ();

        parentStep.add (step);
    }
}
