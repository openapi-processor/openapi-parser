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

import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;
import static io.openapiprocessor.jsonschema.support.Types.*;

public class PropertyDependencies {
    private final Validator validator;

    public PropertyDependencies(Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Map<String, JsonSchema> schemas = schema.getPropertyDependencies ();
        if (schemas == null) {
            return;
        }

        DependentSchemasStep step = new DependentSchemasStep (schema, instance);
        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.forEach((key, value) -> {
            JsonSchema keySchema = schemas.get(key);
            if (keySchema == null) {
                return;
            }

            if (!isString(value)) {
                return;
            }

            JsonSchema valueSchema = keySchema.getPropertyJsonSchema(asString(value));

            DependentSchemaStep depStep = new DependentSchemaStep(valueSchema, instance);
            validator.validate(valueSchema, instance, dynamicScope, depStep);
            step.add(depStep);
        });

        parentStep.add (step);
    }
}
