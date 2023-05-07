/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.DynamicScope;
import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates propertyNames. Since Draft 6.
 */
public class PropertyNames {
    private final Validator validator;

    public PropertyNames (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        JsonSchema propertyNames = schema.getPropertyNames ();
        if (propertyNames == null || !instance.isObject ())
            return;

        PropertyNamesStep step = new PropertyNamesStep (schema, instance);

        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        for (String instanceProperty : instanceProperties) {
            JsonInstance propertyName = instance.getPropertyName (instanceProperty);
            validator.validate (propertyNames, propertyName, dynamicScope, step);
        }

        parentStep.add (step);
    }
}
