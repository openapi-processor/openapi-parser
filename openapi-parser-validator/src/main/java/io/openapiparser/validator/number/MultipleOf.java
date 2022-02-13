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
 * validates multipleOf.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.1">
 *     Draft 4: multipleOf
 * </a>
 */
public class MultipleOf {

    public Collection<ValidationMessage> validate (JsonSchema schema, JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number multipleOf = schema.getMultipleOf ();
        if (multipleOf == null)
            return messages;

        Number instanceValue = instance.asNumber();
        boolean invalid = new BigDecimal (instanceValue.toString ())
            .remainder (new BigDecimal (multipleOf.toString ()))
            .compareTo (BigDecimal.ZERO) != 0;

        if (invalid) {
            messages.add (new MultipleOfError (instance.getPath (), multipleOf));
        }

        return messages;
    }
}
