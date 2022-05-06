/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.*;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates propertyNames.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.22">
 *     propertyNames</a>,
 */
public class PropertyNames {
    private final Validator validator;

    public PropertyNames (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        JsonSchema propertyNames = schema.getPropertyNames ();
        if (propertyNames == null || !instance.isObject ())
            return new NullStep();

        PropertyNamesStep step = new PropertyNamesStep (schema, instance);

        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        for (String instanceProperty : instanceProperties) {
            JsonInstance propertyName = instance.getPropertyName (instanceProperty);
            step.add (validator.validate (propertyNames, propertyName));
        }

        return step;
    }
}
