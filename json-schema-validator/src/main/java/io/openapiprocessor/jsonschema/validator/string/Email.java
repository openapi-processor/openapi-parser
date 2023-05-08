/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Vocabularies;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.validator.support.EmailValidator;
import io.openapiprocessor.jsonschema.schema.Format;

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
        if (!hasFormat (schema))
            return;

        EmailStep step = new EmailStep (schema, instance);

        if (shouldValidate (schema)) {
            String instanceValue = getInstanceValue (instance);
            boolean valid = new EmailValidator (instanceValue).validate ();

            if (!valid)
                step.setInvalid ();
        }

        if (shouldAnnotate (schema)) {
            step.createAnnotation ();
        }

        parentStep.add (step);
    }

    private boolean shouldAnnotate (JsonSchema schema) {
        return getVocabularies (schema).hasFormatAnnotation ();
    }

    private boolean shouldValidate (JsonSchema schema) {
        return getVocabularies (schema).hasFormatAssertion ()
            && settings.validateFormat (Format.EMAIL);
    }

    private boolean hasFormat (JsonSchema schema) {
        String format = schema.getFormat ();
        return format != null
            && format.equals (Format.EMAIL.getFormat ());
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }

    private Vocabularies getVocabularies (JsonSchema schema) {
        JsonSchema metaSchema = schema.getMetaSchemaSchema ();
        if (metaSchema == null) {
            return Vocabularies.ALL;
        }

        Vocabularies vocabularies = metaSchema.getVocabulary ();
        if (vocabularies == null) {
            return Vocabularies.ALL;
        }

        return vocabularies;
    }
}
