/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.*;

import java.util.*;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates dependencies. Since Draft 4.
 */
public class Dependencies {
    private final Validator validator;

    public Dependencies (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Map<String, JsonDependency> dependencies = schema.getDependencies ();
        if (dependencies == null) {
            return;
        }

        DependenciesStep step = new DependenciesStep (schema, instance);

        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            JsonDependency propDependency = dependencies.get (propName);
            if (propDependency != null) {
                if (propDependency.isSchema ()) {
                    validator.validate (propDependency.getSchema (), instance, dynamicScope, step);
                } else {
                    Set<String> instanceProperties = new HashSet<> (instanceObject.keySet ());

                    propDependency.getProperties ().forEach ( p -> {
                        if (!instanceProperties.contains (p)) {
                            DependencyStep depStep = new DependencyStep (schema, instance, p);
                            depStep.setInvalid ();
                            step.add (depStep);
                        }
                    });
                }
            }
        });

        parentStep.add (step);
    }
}
