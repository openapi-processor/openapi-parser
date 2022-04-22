/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.*;

import java.util.*;

/**
 * validates required.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.17">
 *     required</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.3">
 *     required</a>
 */
public class Required {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Collection<String> requiredProperties = schema.getRequired ();

        if (requiredProperties == null)
            return new NullStep ();

        RequiredStep step = new RequiredStep (schema, instance);
        requiredProperties.forEach (p -> {
            boolean found = instanceProperties.contains (p);
            if (found) {
                return;
            }

            RequireStep rStep = new RequireStep (schema, instance, p);
            rStep.setInvalid ();
            step.add (rStep);
        });

        return step;
    }
}
