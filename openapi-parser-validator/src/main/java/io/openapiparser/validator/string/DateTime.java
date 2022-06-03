/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static io.openapiparser.schema.Format.DATE;
import static io.openapiparser.schema.Format.DATE_TIME;
import static io.openapiparser.support.Nullness.nonNull;

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
        Format format = Format.of (schema.getFormat ());
        if (!shouldValidate (format))
            return new NullStep ();

        DateTimeStep step = new DateTimeStep (schema, instance);

        try {
            getParser (format).parse (getInstanceValue (instance));

        } catch (Exception ex) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable Format format) {
        return format != null && settings.validateFormat (format);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }

    private Parser<?> getParser (Format format) {
        if (format.equals (DATE_TIME))
            return new DateTimeParser ();
        else if (format.equals (DATE))
            return new DateParser ();
        else
            // todo
            throw new RuntimeException ();
    }

    private interface Parser<T> {
        T parse (String value);
    }
    private static class DateTimeParser implements Parser<OffsetDateTime> {
        @Override
        public OffsetDateTime parse (String value) {
            return DateTimeFormatter.ISO_DATE_TIME.parse (value, OffsetDateTime::from);
        }
    }

    private static class DateParser implements Parser<LocalDate> {
        @Override
        public LocalDate parse (String value) {
            return DateTimeFormatter.ISO_DATE.parse (value, LocalDate::from);
        }
    }
}
