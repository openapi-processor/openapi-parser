/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.*;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

import java.net.URI;

import static io.openapiparser.support.Nullness.nonNull;

public class DynamicRefStep extends CompositeStep {
    private final JsonSchema schema;
    protected final JsonInstance instance;
    private final String keyword;

    public DynamicRefStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
        keyword = isDraft201909 (schema) ? Keywords.RECURSIVE_REF : Keywords.DYNAMIC_REF;
    }

    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (keyword);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope(), getKeywordLocation ());
    }

    public URI getRef () {
        return nonNull(schema.getDynamicRef ());
    }

    public String toString () {
        return Step.toString (
            getKeywordLocation (),
            getInstanceLocation (),
            isValid ());
    }

    @UnderInitialization
    private boolean isDraft201909 (JsonSchema schema) {
        return SchemaVersion.Draft201909.equals (schema.getContext ().getVersion ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}
