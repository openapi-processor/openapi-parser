/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;

import java.util.*;

public class ContainsStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private Annotation annotation;
    private boolean valid = true;

    public ContainsStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    public void setInvalid () {
        valid = false;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (valid)
            return Collections.emptyList ();

        return Collections.singletonList (getError());
    }

    public void addAnnotation (Collection<Integer> annotation) {
        this.annotation = new Annotation ("contains", annotation);
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        Collection<Annotation> composite = super.getAnnotations (keyword);
        if (!keyword.equals ("contains")) {
            return composite;
        }

        Collection<Annotation> annotations = new ArrayList<> (composite);
        annotations.add (annotation);
        return annotations;
    }

    private ValidationMessage getError () {
        return new ContainsError (schema, instance, super.getMessages ());
    }
}
