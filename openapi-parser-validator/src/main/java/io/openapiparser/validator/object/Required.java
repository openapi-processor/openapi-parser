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
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.3">
 *     Draft 4: required
 * </a>
 */
public class Required {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Collection<String> requiredProperties = schema.getRequired ();

        if (requiredProperties == null)
            return new NullStep ();

        CompositeStep step = new RequiredStep ();
        requiredProperties.forEach (p -> {
            boolean found = instanceProperties.contains (p);
            if (found) {
                return;
            }

            step.add (new ErrorStep (new RequiredError (instance.getPath (), p)));
        });

        return step;
    }
}
