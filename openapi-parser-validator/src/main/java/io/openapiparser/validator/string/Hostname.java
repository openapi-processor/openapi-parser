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
 * validates hostname.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.3">
 *     Draft 4: hostname
 * </a>
 */
public class Hostname {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("hostname"))
            return messages;

        String instanceValue = instance.asString ();
        boolean valid = isValid (instanceValue);
        if (!valid) {
            messages.add (new HostnameError (instance.getPath ()));
        }

        return messages;
    }

    boolean isValid(String ip) {
        String[] labels = ip.split ("\\.");
        if (labels.length == 0) {
            return false;
        }

        for (String value : labels) {
            if (!value.codePoints ().allMatch (this::isValidChar)) {
                return false;
            }

            if (value.startsWith ("-") || value.endsWith ("-")) {
                return false;
            }

            if (value.length () > 63) {
                return false;
            }
        }

        return true;
    }

    boolean isValidChar (int c) {
        return isNumber (c) || isLetter (c) || isDash (c);
    }

    boolean isNumber (int c) {
        return c >= 48 && c <= 57;
    }

    boolean isLetter (int c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    boolean isDash (int c) {
        return c == 45;
    }
}