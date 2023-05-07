/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Vocabularies;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.EmailValidator;

import static io.openapiparser.schema.Format.EMAIL;
import static io.openapiparser.support.Nullness.nonNull;

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
            && settings.validateFormat (EMAIL);
    }

    private boolean hasFormat (JsonSchema schema) {
        String format = schema.getFormat ();
        return format != null
            && format.equals (EMAIL.getFormat ());
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
