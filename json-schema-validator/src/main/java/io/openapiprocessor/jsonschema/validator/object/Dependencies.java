/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.DynamicScope;
import io.openapiprocessor.jsonschema.schema.JsonDependency;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

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
                        if (!instanceProperties.contains (nonNull(p))) {
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
