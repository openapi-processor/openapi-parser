/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.validator.support.IpV6Validator;
import io.openapiprocessor.jsonschema.schema.Format;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates ipv6. Since Draft4.
 */
public class IpV6 {
    private final ValidatorSettings settings;

    public IpV6 (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        IpV6Step step = new IpV6Step (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new IpV6Validator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (Format.IPV6.getFormat ())
            && settings.validateFormat (Format.IPV6);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull(instance.asString ());
    }
}
