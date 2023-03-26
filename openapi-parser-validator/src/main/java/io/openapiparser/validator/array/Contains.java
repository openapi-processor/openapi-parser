/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.*;

import java.util.Collection;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates contains, minContains &amp; maxContains.
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

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        JsonSchema contains = schema.getContains ();
        if (contains == null)
            return new NullStep ("contains");

        CompositeStep step = new FlatStep ();

        Collection<Object> instanceValue = getInstanceValue (instance);
        int instanceSize = instanceValue.size ();

        ContainsStep containsStep = new ContainsStep (schema, instance);
        step.add (containsStep);

        int validCount = 0;
        for (int idx = 0; idx < instanceSize; idx++) {
            JsonInstance value = instance.getValue (idx);
            ValidationStep validate = validator.validate (contains, value, dynamicScope);
            containsStep.add (validate);

            if (validate.isValid ()) {
                validCount++;
            }
        }

        boolean containsIsValid = validCount > 0;
        Integer minContains = schema.getMinContains ();
        Integer maxContains = schema.getMaxContains ();

        if (minContains != null) {
            MinContainsStep minStep = new MinContainsStep (schema, instance);
            minStep.setValid (validCount >= minContains);
            step.add (minStep);

            if (minContains == 0 && maxContains == null) {
                containsIsValid = true;
            }
        }

        if (maxContains != null) {
            MaxContainsStep minStep = new MaxContainsStep (schema, instance);
            boolean valid = validCount <= maxContains;
            minStep.setValid (valid);
            step.add (minStep);

            if (valid && minContains != null && minContains == 0) {
                containsIsValid = true;
            }
        }

        if (!containsIsValid) {
            containsStep.setInvalid ();
        }

        return step;
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
