/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * validates minItems.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.3">
 *     Draft 4: minItems
 * </a>
 */
public class MinItems {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Collection<Object> instanceValue = instance.asCollection ();
        int minItems = schema.getMinItems ();
        if (instanceValue.size () < minItems) {
            messages.add (new MinItemsError (instance.getPath (), minItems));
        }

        return messages;
    }
}
