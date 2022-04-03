/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.regex.Matcher;

/**
 * validates email.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.2">
 *     Draft 4: email
 * </a>
 *
 * todo replace regex with validation code
 */
public class Email {
    // https://stackoverflow.com/a/48725527
    private static final String EMAIL_REGEX =
        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
        "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (format == null || !format.equals ("email"))
            return new NullStep ();

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(EMAIL_REGEX);
        String instanceValue = instance.asString ();
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();

        if (valid)
            return new EmailStep ();

        return new EmailStep (new EmailError (instance.getPath ()));
    }
}
