/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import static io.openapiparser.schema.UriSupport.resolve;

public abstract class SimpleStep implements ValidationStep {
    protected final JsonSchema schema;
    protected final JsonInstance instance;
    protected final String property;
    private boolean valid = true;

    public SimpleStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
        this.property = "n/a";
    }

    public SimpleStep (JsonSchema schema, JsonInstance instance, String property) {
        this.schema = schema;
        this.instance = instance;
        this.property = property;
    }

    protected abstract ValidationMessage getError ();

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (valid)
            return Collections.emptyList ();

        return Collections.singletonList (getError());
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

    public JsonPointer getInstanceLocation () {
        return instance.getLocation ().append (property);
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (property);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        JsonSchemaContext context = schema.getContext ();
        Scope scope = context.getScope ();

        JsonPointer location = getKeywordLocation ();
        return resolve (scope.getBaseUri (), location.toUri ());
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.toString ().isEmpty () ? "/" : instance.toString (),
            schema.getLocation ().append (property));
    }
}
