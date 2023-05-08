/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class JsonInstanceContext {

    private final Scope scope;
    private final ReferenceRegistry references;

    public JsonInstanceContext (Scope scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
    }

    public Scope getScope () {
        return scope;
    }
}
