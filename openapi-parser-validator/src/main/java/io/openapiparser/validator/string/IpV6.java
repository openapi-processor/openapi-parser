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
import io.openapiparser.validator.support.IpV6Validator;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates ipv6.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.5">
 *     ipv6</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.5">
 *     ipv6</a>
 */
public class IpV6 {
    private final ValidatorSettings settings;

    public IpV6 (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        IpV6Step step = new IpV6Step (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new IpV6Validator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals ("ipv6")
            && settings.validateFormat ("ipv6");
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull(instance.asString ());
    }
}
