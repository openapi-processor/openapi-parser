/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.steps;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Scope;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public abstract class SimpleStep implements ValidationStep {
    protected final JsonSchema schema;
    protected final JsonInstance instance;
    protected final String property;
    private boolean valid = true;

    public SimpleStep (JsonSchema schema, JsonInstance instance, String property) {
        this.schema = schema;
        this.instance = instance;
        this.property = property;
    }

    protected abstract ValidationMessage getError ();

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.emptyList ();
    }

    @Override
    public void add (ValidationStep step) {
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
        return getError ();
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        return null;
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return Collections.emptyList ();
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    public void setInvalid () {
        valid = false;
    }

    public void setValid (boolean valid) {
        this.valid = valid;
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (property);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    protected Scope getScope () {
        return schema.getContext ().getScope ();
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), valid);
    }
}
