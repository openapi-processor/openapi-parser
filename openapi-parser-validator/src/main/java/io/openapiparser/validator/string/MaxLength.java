/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * validates maxLength.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.1">
 *     Draft 4: maxLength
 * </a>
 */
public class MaxLength {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Integer maxLength = schema.getMaxLength ();
        if (maxLength == null)
            return messages;

        String instanceValue = instance.asString ();
        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) <= maxLength;
        if (!valid) {
            messages.add (new MaxLengthError(instance.getPath (), maxLength));
        }

        return messages;
    }
}
