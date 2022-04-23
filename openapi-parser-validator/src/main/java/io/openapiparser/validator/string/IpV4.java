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
import io.openapiparser.validator.support.IpV4Validator;

/**
 * validates ipv4.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.4">
 *     ipv4</a>

 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.4">
 *     ipv4</a>
 */
public class IpV4 {
    private final ValidatorSettings settings;

    public IpV4 (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        IpV4Step step = new IpV4Step (schema, instance);

        String instanceValue = instance.asString ();
        boolean valid = new IpV4Validator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (String format) {
        return format != null
            && format.equals ("ipv4")
            && settings.validateFormat ("ipv4");
    }
}
