/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.UriValidator;

/**
 * validates format: uri.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.6">
 *     uri</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.6">
 *     uri</a>
 */
public class Uri {
    private final ValidatorSettings settings;

    public Uri (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        UriStep step = new UriStep (schema, instance);

        boolean valid = new UriValidator (instance.asString ()).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (String format) {
        return format != null
            && format.equals ("uri")
            && settings.validateFormat ("uri");
    }
}
