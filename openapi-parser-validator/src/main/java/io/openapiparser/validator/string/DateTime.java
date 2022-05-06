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
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * validates format: date-time (except leap seconds).
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.1">
 *     date-time</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.1">
 *     date-time</a>
 */
public class DateTime {
    private final ValidatorSettings settings;

    public DateTime (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        DateTimeStep step = new DateTimeStep (schema, instance);

        String instanceValue = instance.asString ();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        try {
            formatter.parse (instanceValue, OffsetDateTime::from);

        } catch (RuntimeException ex) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals ("date-time")
            && settings.validateFormat ("date-time");
    }
}
