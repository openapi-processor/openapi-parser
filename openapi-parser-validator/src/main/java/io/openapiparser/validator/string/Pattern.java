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
import java.util.regex.Matcher;

/**
 * validates pattern.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.3">
 *     Draft 4: pattern
 * </a>
 */
public class Pattern {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String pattern = schema.getPattern ();
        if (pattern == null)
            return messages;

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        String instanceValue = instance.asString ();
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();
        if (!valid) {
            messages.add (new PatternError(instance.getPath (), pattern));
        }

        return messages;
    }
}
