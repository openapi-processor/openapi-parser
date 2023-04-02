/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.*;

import static io.openapiparser.schema.Keywords.DEPENDENT_REQUIRED;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates dependentRequired.
 *
 * <p>See specification:
 *
 * <p>Draft 2019-09:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-02#section-6.5.4">
 *     dependentRequired</a>
 */
public class DependentRequired {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        DependenciesStep step = new DependenciesStep (schema, instance);

        if (!shouldValidate(schema)) {
            return step;
        }

        Map<String, Set<String>> required = schema.getDependentRequired ();
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

        return step;
    }

    private boolean shouldValidate(JsonSchema schema) {
        // schema.getVocabulary(keyword).enabled
        // schema.getVocabularyValidation()
        return schema.getContext().getVersion().getKeyword(DEPENDENT_REQUIRED) != null;
    }
}
