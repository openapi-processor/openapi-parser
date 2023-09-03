/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.steps;

import io.openapiprocessor.jsonschema.schema.*;

import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

public class RefStep extends CompositeStep {
    private final JsonSchema schema;
    protected final JsonInstance instance;

    public RefStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (Keywords.REF);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope(), getKeywordLocation ());
    }

    public URI getRef () {
        return nonNull(schema.getRef ());
    }

    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}
