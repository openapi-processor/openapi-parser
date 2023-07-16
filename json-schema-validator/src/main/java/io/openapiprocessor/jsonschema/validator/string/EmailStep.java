/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

public class EmailStep extends SimpleStep {

    public EmailStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.FORMAT);
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        String format = schema.getFormat();
        if (format == null || !isValid()) {
            return null;
        }

        return new Annotation(Keywords.FORMAT, format);
    }

    @Override
    public Collection<Annotation> getAnnotations(String keyword) {
        Annotation annotation = getAnnotation();
        if (annotation == null)
            return List.of();

        return List.of(annotation);
    }

    @Override
    protected ValidationMessage getError () {
        return new EmailError (schema, instance);
    }
}
