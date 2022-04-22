/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.*;

import java.util.*;

/**
 * validate uniqueItems.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.13">
 *     uniqueItems</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.4">
 *     uniqueItems</a>
 */
public class UniqueItems {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        UniqueItemsStep step = new UniqueItemsStep (schema, instance);

        Collection<Object> instanceValue = instance.asCollection ();
        if (schema.isUniqueItems ()) {
            Set<Object> items = new HashSet<> ();
            for (Object item : instanceValue) {
                if (!items.add (item)) {
                    step.setInvalid ();
                    return step;
                }
            }
        }

        return step;
    }
}
