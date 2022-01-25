/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates minLength.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.2">
 *     Draft 4: minLength
 * </a>
 */
public class MinLength {
    private final URI uri;
    private final JsonSchema schema;

    public MinLength (URI uri, JsonSchema schema) {
        this.uri = uri;
        this.schema = schema;
    }

    public Collection<ValidationMessage> validate (String source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Integer minLength = schema.getMinLength ();

        boolean valid = source.codePointCount (0, source.length ()) >= minLength;
        if (!valid) {
            messages.add (new MinLengthError(uri.toString (), minLength));
        }

        return messages;
    }
}
