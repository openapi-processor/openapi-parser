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
 * maximum and exclusiveMaximum.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.2">
 *     Draft 4: maximum and exclusiveMaximum
 * </a>
 */
public class Maximum {
    private final URI uri;
    private final JsonSchema schema;

    public Maximum (URI uri, JsonSchema schema) {
        this.uri = uri;
        this.schema = schema;
    }

    public Collection<ValidationMessage> validate (Number source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number maximum = schema.getMaximum ();
        Boolean exclusive = schema.getExclusiveMaximum ();

        if (maximum == null)
            return messages;

        boolean valid;
        if (exclusive) {
            valid = compareTo (source, maximum) < 0;
        } else {
            valid = compareTo (source, maximum) <= 0;
        }

        if (!valid) {
            messages.add (new MaximumError (uri.toString (), maximum, exclusive));
        }

        return messages;
    }

    private int compareTo (Number source, Number maximum) {
        return new BigDecimal (source.toString ())
            .compareTo (new BigDecimal (maximum.toString ()));
    }
}
