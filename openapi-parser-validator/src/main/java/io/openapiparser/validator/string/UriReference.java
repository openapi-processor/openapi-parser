/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.UriValidator;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.schema.Format.URI_REFERENCE;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates format: uri-reference.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.7">
 *     uri-reference</a>
 */
public class UriReference {
    private final ValidatorSettings settings;

    public UriReference (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        UriStep step = new UriStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new UriValidator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (URI_REFERENCE.getFormat ())
            && settings.validateFormat (URI_REFERENCE);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}
