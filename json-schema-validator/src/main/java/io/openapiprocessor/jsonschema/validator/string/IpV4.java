/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.validator.support.IpV4Validator;
import io.openapiprocessor.jsonschema.schema.Format;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates ipv4. Since Draft 4.
 */
public class IpV4 {
    private final ValidatorSettings settings;

    public IpV4 (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        IpV4Step step = new IpV4Step (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new IpV4Validator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (Format.IPV4.getFormat ())
            && settings.validateFormat (Format.IPV4);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull(instance.asString ());
    }
}
