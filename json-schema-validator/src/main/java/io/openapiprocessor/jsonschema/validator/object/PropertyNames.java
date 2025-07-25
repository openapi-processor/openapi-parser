/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.DynamicScope;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

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

        Map<String, @Nullable Object> instanceObject = requiresNonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        for (String instanceProperty : instanceProperties) {
            JsonInstance propertyName = instance.getPropertyName (instanceProperty);
            validator.validate (propertyNames, propertyName, dynamicScope, step);
        }

        parentStep.add (step);
    }
}
