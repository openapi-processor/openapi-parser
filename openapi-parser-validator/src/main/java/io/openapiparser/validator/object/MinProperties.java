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

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates minProperties.
 * <p>Specification: since Draft 4
 */
public class MinProperties {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());
        Integer minProperties = schema.getMinProperties ();

        if (minProperties == null)
            return new NullStep ("minProperties");

        MinPropertiesStep step = new MinPropertiesStep (schema, instance);
        if (instanceProperties.size () < minProperties) {
            step.setInvalid ();
        }

        return step;
    }
}
