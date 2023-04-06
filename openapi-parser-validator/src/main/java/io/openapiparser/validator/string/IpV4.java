/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.IpV4Validator;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.schema.Format.IPV4;
import static io.openapiparser.support.Nullness.nonNull;

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
            return new NullStep ("ipv4");

        IpV4Step step = new IpV4Step (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new IpV4Validator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (IPV4.getFormat ())
            && settings.validateFormat (IPV4);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull(instance.asString ());
    }
}
