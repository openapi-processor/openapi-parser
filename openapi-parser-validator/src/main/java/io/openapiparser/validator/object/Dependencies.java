/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.*;

import java.util.*;

/**
 * validates dependencies.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.5">
 *     Draft 4: dependencies
 * </a>
 */
public class Dependencies {
    private final Validator validator;

    public Dependencies (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        CompositeStep step = new DependenciesStep ();

        Map<String, JsonDependency> dependencies = schema.getDependencies ();
        instance.asObject ().forEach ((propName, unused) -> {
            JsonDependency propDependency = dependencies.get (propName);
            if (propDependency != null) {
                if (propDependency.isSchema ()) {
                    step.add (validator.validate (propDependency.getSchema (), instance));
                } else {
                    Set<String> instanceProperties = new HashSet<> (instance.asObject ().keySet ());

                    propDependency.getProperties ().forEach ( p -> {
                        if (!instanceProperties.contains (p)) {
                            step.add (new ErrorStep (new DependenciesError (instance.getPath (), p)));
                        }
                    });
                }
            }
        });

        return step;
    }
}
