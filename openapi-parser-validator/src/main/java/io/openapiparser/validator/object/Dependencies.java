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
 * validates dependencies.
 *
 * <p>See specification:
 *
 * <p>Draft 7:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.5.7">
 *     dependencies</a>
 *
 * <br>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.21">
 *     dependencies</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.5">
 *     dependencies</a>
 */
public class Dependencies {
    private final Validator validator;

    public Dependencies (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        Map<String, JsonDependency> dependencies = schema.getDependencies ();
        if (dependencies == null) {
            return new NullStep ("dependencies");
        }

        DependenciesStep step = new DependenciesStep (schema, instance);

        Map<String, Object> instanceObject = nonNull(instance.asObject ());

        instanceObject.keySet ().forEach (propName -> {
            JsonDependency propDependency = dependencies.get (propName);
            if (propDependency != null) {
                if (propDependency.isSchema ()) {
                    step.add (validator.validate (propDependency.getSchema (), instance, dynamicScope));
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

        return step;
    }
}
