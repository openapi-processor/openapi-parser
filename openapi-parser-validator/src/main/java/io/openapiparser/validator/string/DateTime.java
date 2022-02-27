/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates format: date-time.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.1">
 *     Draft 4: date-time (except leap seconds)
 * </a>
 */
public class DateTime {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("date-time"))
            return messages;

        String instanceValue = instance.asString ();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        try {
            formatter.parse (instanceValue, OffsetDateTime::from);

        } catch (RuntimeException ex) {
            messages.add (new DateTimeError (instance.getPath ()));
        }

        return messages;
    }
}
