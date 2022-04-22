/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.*;

/**
 * validates maxProperties.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.15">
 *     maxProperties</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.1">
 *     maxProperties</a>
 */
public class MaxProperties {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Integer maxProperties = schema.getMaxProperties ();

        if (maxProperties == null)
            return new NullStep ();

        MaxPropertiesStep step = new MaxPropertiesStep (schema, instance);

        if (instanceProperties.size () > maxProperties)
            step.setInvalid ();

        return step;
    }
}
