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
 * validates minProperties.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.2">
 *     Draft 4: minProperties
 * </a>
 */
public class MinProperties {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Integer minProperties = schema.getMinProperties ();

        if (minProperties == null)
            return new NullStep ();

        if (instanceProperties.size () >= minProperties)
            return new MinPropertiesStep ();

        return new MinPropertiesStep (new MinPropertiesError (instance.getPath (), minProperties));
    }
}
