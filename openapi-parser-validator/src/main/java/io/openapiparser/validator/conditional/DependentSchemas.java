/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.conditional;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.object.PropertyStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Map;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates dependentSchemas.
 *
 * <p>See specification:
 *
 * <p>Draft 2019-09:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.2.2.4">
 *     dependentSchemas</a>
 *
 * todo move
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-02#section-6.5.4">
 *     dependentRequired</a>
 */
public class DependentSchemas {
    private final Validator validator;

    public DependentSchemas (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        DependentSchemasStep step = new DependentSchemasStep ("dependentSchemas");

        Map<String, JsonSchema> schemas = schema.getDependentSchemas ();
        if (schemas.isEmpty ()) {
            return step;
        }

        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            JsonSchema propSchema = schemas.get (propName);
            if (propSchema == null) {
                return;
            }

            ValidationStep propStep = validator.validate (propSchema, instance, dynamicScope);
            step.add (new PropertyStep (propName, propStep));
        });

        return step;
    }
}
