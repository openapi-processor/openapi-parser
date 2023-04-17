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
 * validates minProperties. Since Draft 4
 */
public class MinProperties {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        Integer minProperties = schema.getMinProperties ();
        if (minProperties == null)
            return;

        MinPropertiesStep step = new MinPropertiesStep (schema, instance);
        if (instanceProperties.size () < minProperties) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }
}
