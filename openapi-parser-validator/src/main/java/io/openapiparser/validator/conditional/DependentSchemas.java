/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.conditional;

import io.openapiparser.schema.DynamicScope;
import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Map;

import static io.openapiparser.support.Nullness.nonNull;

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

        DependentSchemasStep step = new DependentSchemasStep ("dependentSchemas");
        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            JsonSchema propSchema = schemas.get (propName);
            if (propSchema == null) {
                return;
            }

            validator.validate (propSchema, instance, dynamicScope, step);
            //step.add (new PropertyStep (propSchema, instance, propName, propStep));
        });

        parentStep.add (step);
    }
}
