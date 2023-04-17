/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.*;

import java.util.*;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validate uniqueItems. Since Draft 4.
 */
public class UniqueItems {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        UniqueItemsStep step = new UniqueItemsStep (schema, instance);

        Collection<Object> instanceValue = getInstanceValue (instance);
        if (schema.isUniqueItems ()) {
            Set<Object> items = new HashSet<> ();
            for (Object item : instanceValue) {
                if (!items.add (item)) {
                    step.setInvalid ();
                    break;
                }
            }
        }

        parentStep.add (step);
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}
