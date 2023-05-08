/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Scope;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.CompositeStep;
import io.openapiprocessor.jsonschema.validator.steps.Step;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public class ContainsStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private @Nullable Annotation annotation;
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
    public @Nullable ValidationMessage getMessage () {
        if (valid)
            return null;

        return new ContainsError (schema, instance);
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        return annotation;
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
        if (annotation != null) {
            annotations.add (annotation);
        }
        return annotations;
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ();
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}
