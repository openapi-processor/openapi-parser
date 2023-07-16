/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.Format;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.validator.support.EmailValidator;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates email. Since Draft 4.
 */
public class Email {
    private final ValidatorSettings settings;

    public Email (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Format format = Format.of (schema.getFormat ());
        if (format == null || !isEmailFormat(format))
            return;

        EmailStep step = new EmailStep (schema, instance);
        parentStep.add (step);

        if (!shouldValidate (format, schema)) {
            return;
        }

        String instanceValue = getInstanceValue (instance);
        boolean valid = new EmailValidator (instanceValue).validate ();
        if (!valid) {
             step.setInvalid ();
        }
    }

    private boolean shouldValidate (Format format, JsonSchema schema) {
        boolean shouldAssert = assertFormat(schema);
        if (!shouldAssert) {
            shouldAssert = settings.assertFormat();
        }

        return shouldAssert;
    }

    private boolean assertFormat(JsonSchema schema) {
        return schema.getContext().getVocabularies().requiresFormatAssertion();
    }

    private boolean isEmailFormat (Format format) {
        return Format.EMAIL.equals (format);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}
