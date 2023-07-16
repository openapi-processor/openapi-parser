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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates format: date-time, date, time (except leap seconds). Since Draft 4.
 */
public class DateTime {
    private final ValidatorSettings settings;

    public DateTime (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Format format = Format.of (schema.getFormat ());
        if (format == null || !isTimeFormat(format))
            return;

        DateTimeStep step = new DateTimeStep (schema, instance);
        parentStep.add (step);

        if (!shouldValidate (schema)) {
            return;
        }

        try {
            getParser (format).parse (getInstanceValue (instance));

        } catch (Exception ex) {
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

    private static boolean isTimeFormat(Format format) {
        return Format.DATE_TIME.equals(format)
            || Format.DATE.equals(format)
            || Format.TIME.equals(format);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }

    private Parser<?> getParser (Format format) {
        if (format.equals (Format.DATE_TIME))
            return new DateTimeParser ();
        else if (format.equals (Format.DATE))
            return new DateParser ();
        else if (format.equals (Format.TIME))
            return new TimeParser ();
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

    private static class TimeParser implements Parser<OffsetTime> {
        @Override
        public OffsetTime parse (String value) {
            return DateTimeFormatter.ISO_OFFSET_TIME.parse (value, OffsetTime::from);
        }
    }
}
