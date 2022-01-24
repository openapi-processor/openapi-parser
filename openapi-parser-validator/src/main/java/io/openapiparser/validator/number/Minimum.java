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
 * validates minimum and exclusiveMinimum.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.3">
 *     Draft 4: minimum and exclusiveMinimum
 * </a>
 */
public class Minimum {
    private final URI uri;
    private final JsonSchema schema;

    public Minimum (URI uri, JsonSchema schema) {
        this.uri = uri;
        this.schema = schema;
    }

    public Collection<ValidationMessage> validate (Number source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number minimum = schema.getMinimum ();
        Boolean exclusive = schema.getExclusiveMinimum ();

        if (minimum == null)
            return messages;

        boolean valid;
        if (exclusive) {
            valid = compareTo (source, minimum) > 0;
        } else {
            valid = compareTo (source, minimum) >= 0;
        }

        if (!valid) {
            messages.add (new MinimumError (uri.toString (), minimum, exclusive));
        }

        return messages;
    }

    private int compareTo (Number source, Number minimum) {
        return new BigDecimal (source.toString ())
            .compareTo (new BigDecimal (minimum.toString ()));
    }
}
