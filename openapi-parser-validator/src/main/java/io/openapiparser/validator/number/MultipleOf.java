/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.messages.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates multipleOf.
 *
 * <p>See specification:
 * <ul>
 *     <li>
 *      <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.1">Draft 4: multipleOf</a>
 *     </li>
 * </ul>
 */
public class MultipleOf {
    private final URI uri;

    public MultipleOf (URI uri) {
        this.uri = uri;
    }

    public Collection<ValidationMessage> validate (JsonSchema schema, Number source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Number multipleOf = schema.getMultipleOf ();
        if (multipleOf == null)
            return messages;

        boolean invalid = new BigDecimal (source.toString ())
            .remainder (new BigDecimal (multipleOf.toString ()))
            .compareTo (BigDecimal.ZERO) != 0;

        if (invalid) {
            messages.add (new MultipleOfError (uri.toString (), multipleOf));
        }

        return messages;
    }
}
