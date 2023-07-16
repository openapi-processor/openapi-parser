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
import io.openapiprocessor.jsonschema.validator.support.UriValidator;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates format: uri-reference. Since Draft 6.
 */
public class UriReference {
    private final ValidatorSettings settings;

    public UriReference (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Format format = Format.of (schema.getFormat ());
        if (format == null || !supportsFormat(format))
            return;

        UriStep step = new UriStep (schema, instance);
        parentStep.add (step);

        if (!shouldValidate (schema)) {
            return;
        }

        String instanceValue = getInstanceValue (instance);
        boolean valid = new UriValidator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }
    }

    private boolean shouldValidate (JsonSchema schema) {
        boolean shouldAssert = schema.getContext().getVocabularies().requiresFormatAssertion();
        if (!shouldAssert) {
            shouldAssert = settings.assertFormat();
        }

        return shouldAssert;
    }

    private boolean supportsFormat(Format format) {
        return Format.URI_REFERENCE.equals (format);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}
