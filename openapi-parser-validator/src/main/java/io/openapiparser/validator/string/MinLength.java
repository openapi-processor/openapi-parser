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
 * validates minLength.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.2">
 *     Draft 4: minLength
 * </a>
 */
public class MinLength {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Integer minLength = schema.getMinLength ();

        String instanceValue = instance.asString ();
        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) >= minLength;
        if (!valid) {
            messages.add (new MinLengthError(instance.getPath (), minLength));
        }

        return messages;
    }
}
