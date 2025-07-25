/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.conditional;

import io.openapiprocessor.jsonschema.schema.DynamicScope;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

/**
 * validates dependentSchemas. Since Draft 2019-09.
 */
public class DependentSchemas {
    private final Validator validator;

    public DependentSchemas (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Map<String, JsonSchema> schemas = schema.getDependentSchemas ();
        if (schemas == null) {
            return;
        }

        DependentSchemasStep step = new DependentSchemasStep (schema, instance);
        Map<String, @Nullable Object> instanceObject = requiresNonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            JsonSchema propSchema = schemas.get (propName);
            if (propSchema == null) {
                return;
            }

            DependentSchemaStep depStep = new DependentSchemaStep (propSchema, instance);
            validator.validate (propSchema, instance, dynamicScope, depStep);
            step.add (depStep);
        });

        parentStep.add (step);
    }
}
