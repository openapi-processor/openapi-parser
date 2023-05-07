/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiparser.schema.Keywords.DEPENDENT_REQUIRED;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates dependentRequired. Since Draft 2019-09:
 */
public class DependentRequired {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        DependenciesStep step = new DependenciesStep (schema, instance);

        if (!shouldValidate(schema)) {
            return;
        }

        Map<String, Set<String>> required = schema.getDependentRequired ();
        if (required == null) {
            return;
        }

        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            Set<String> requiredProperties = required.get (propName);
            if (requiredProperties != null) {
                Set<String> instanceProperties = new HashSet<> (instanceObject.keySet ());
                    requiredProperties.forEach ( p -> {
                        if (!instanceProperties.contains (p)) {
                            DependencyStep depStep = new DependencyStep (schema, instance, p);
                            depStep.setInvalid ();
                            step.add (depStep);
                        }
                    });
            }
        });

        parentStep.add (step);
    }

    private boolean shouldValidate(JsonSchema schema) {
        // schema.getVocabulary(keyword).enabled
        // schema.getVocabularyValidation()
        return schema.getContext().getVersion().getKeyword(DEPENDENT_REQUIRED) != null;
    }
}
