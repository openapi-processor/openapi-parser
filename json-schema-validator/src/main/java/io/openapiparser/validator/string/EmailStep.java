/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;
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
