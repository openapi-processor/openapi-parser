/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates contains.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.14">
 *     items</a>
 */
public class Contains {
    private final Validator validator;

    public Contains (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        JsonSchema contains = schema.getContains ();
        if (contains == null)
            return new NullStep ("contains");

        ContainsStep step = new ContainsStep (schema, instance);

        Collection<Object> instanceValue = getInstanceValue (instance);
        int instanceSize = instanceValue.size ();

        for (int idx = 0; idx < instanceSize; idx++) {
            JsonInstance value = instance.getValue (idx);
            ValidationStep validate = validator.validate (contains, value);
            if (validate.isValid ()) {
                return step;
            }
        }

        step.setInvalid ();
        return step;
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
