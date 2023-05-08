/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.DynamicScope;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.SchemaStep;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.ArrayList;
import java.util.Collection;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates contains, minContains &amp; maxContains. Since Draft 6.
 */
public class Contains {
    private final Validator validator;

    public Contains (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        JsonSchema contains = schema.getContains ();
        if (contains == null)
            return;

        Collection<Integer> containsAnnotation = new ArrayList<> ();

        Collection<Object> instanceValue = getInstanceValue (instance);
        int instanceSize = instanceValue.size ();

        ContainsStep containsStep = new ContainsStep (schema, instance);

        int validCount = 0;
        for (int idx = 0; idx < instanceSize; idx++) {
            JsonInstance value = instance.getValue (idx);

            ValidationStep step = new SchemaStep (contains, value);
            validator.validate (contains, value, dynamicScope, step);

            if (step.isValid ()) {
                containsAnnotation.add (idx);
                validCount++;
            }
        }

        boolean containsIsValid = validCount > 0;
        Integer minContains = schema.getMinContains ();
        Integer maxContains = schema.getMaxContains ();

        if (minContains != null) {
            MinContainsStep minStep = new MinContainsStep (schema, instance);
            minStep.setValid (validCount >= minContains);
            parentStep.add (minStep);

            if (minContains == 0 && maxContains == null) {
                containsIsValid = true;
            }
        }

        if (maxContains != null) {
            MaxContainsStep minStep = new MaxContainsStep (schema, instance);
            boolean valid = validCount <= maxContains;
            minStep.setValid (valid);
            parentStep.add (minStep);

            if (valid && minContains != null && minContains == 0) {
                containsIsValid = true;
            }
        }

        if (!containsIsValid) {
            containsStep.setInvalid ();
        }

        containsStep.addAnnotation (containsAnnotation);
        parentStep.add (containsStep);
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
