/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.support.UriValidator;

import java.util.*;

/**
 * validates format: uri.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.6">
 *     Draft 4: uri
 * </a>
 */
public class Uri {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("uri"))
            return messages;

        String instanceValue = instance.asString ();

        boolean valid = new UriValidator (instanceValue).validate ();
        if (!valid) {
            messages.add (new UriError (instance.getPath ()));
        }

        return messages;
    }
}
