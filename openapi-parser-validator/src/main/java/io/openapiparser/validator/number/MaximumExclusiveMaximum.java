/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates multipleOf.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.2">
 *     Draft 4: maximum and exclusiveMaximum
 * </a>
 */
public class MaximumExclusiveMaximum {
    private final URI uri;

    public MaximumExclusiveMaximum (URI uri) {
        this.uri = uri;
    }

    public Collection<ValidationMessage> validate (JsonSchema schema, Number source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number maximum = schema.getMaximum ();
        Boolean exclusive = schema.getExclusiveMaximum ();

        if (maximum == null)
            return messages;

        boolean valid;
        if (exclusive) {
            valid = new BigDecimal (source.toString ())
                .compareTo (new BigDecimal(maximum.toString ())) < 0;
        } else {
            valid = new BigDecimal (source.toString ())
                .compareTo (new BigDecimal(maximum.toString ())) <= 0;
        }

        if (!valid) {
            messages.add (new MaximumError (uri.toString (), maximum, exclusive));
        }

        return messages;
    }

}
