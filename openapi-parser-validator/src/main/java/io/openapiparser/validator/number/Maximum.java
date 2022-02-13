/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * maximum and exclusiveMaximum.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.2">
 *     Draft 4: maximum and exclusiveMaximum
 * </a>
 */
public class Maximum {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number maximum = schema.getMaximum ();
        Boolean exclusive = schema.getExclusiveMaximum ();

        if (maximum == null)
            return messages;

        boolean valid;
        if (exclusive) {
            valid = compareTo (instance, maximum) < 0;
        } else {
            valid = compareTo (instance, maximum) <= 0;
        }

        if (!valid) {
            messages.add (new MaximumError (instance.getPath (), maximum, exclusive));
        }

        return messages;
    }

    private int compareTo (JsonInstance instance, Number maximum) {
        return new BigDecimal (instance.asNumber ().toString ())
            .compareTo (new BigDecimal (maximum.toString ()));
    }
}
