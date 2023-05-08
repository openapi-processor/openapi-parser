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

public class EmailStep extends SimpleStep {
    private @Nullable Annotation annotation;

    public EmailStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.FORMAT);
    }

    @Override
    protected ValidationMessage getError () {
        return new EmailError (schema, instance);
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        return annotation;
    }

    public void createAnnotation () {
        annotation = new Annotation (Keywords.FORMAT, schema.getFormat ());
    }
}
