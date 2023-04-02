/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.Map;

public class JsonSchemaContext {
    private final Scope scope;
    private final ReferenceRegistry references;
    private final Vocabularies vocabularies;

    public JsonSchemaContext (Scope scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
        this.vocabularies = Vocabularies.ALL;
    }

    public JsonSchemaContext (Scope scope, ReferenceRegistry references, Vocabularies vocabularies) {
        this.scope = scope;
        this.references = references;
        this.vocabularies = vocabularies;
    }

    public Scope getScope () {
        return scope;
    }

    public SchemaVersion getVersion () {
        return scope.getVersion ();
    }

    public Vocabularies getVocabularies () {
        return vocabularies;
    }

    public Reference getReference (URI ref) {
        if (ref.isAbsolute ()) {
            return references.getReference (ref);
        }

        // is id reference?  is absolute match?
        URI refId = UriSupport.resolve(scope.getBaseUri(), ref);
        if (references.hasReference (refId)) {
            return references.getReference (refId);
        }

        // is local reference..
        URI refLocal = UriSupport.resolve(scope.getBaseUri (), ref);
        return references.getReference (refLocal);
    }

    public Reference getReference (URI ref, URI dynamicScope) {
        URI resolved = dynamicScope.resolve (ref);
        return references.getReference (resolved);
    }

    public JsonSchemaContext withScope (Scope targetScope) {
        return new JsonSchemaContext (targetScope, references, vocabularies);
    }

    public JsonSchemaContext withId (Map<String, Object> properties) {
        return new JsonSchemaContext (scope.move (properties), references, vocabularies);
    }

    public boolean refAllowsSiblings () {
        return scope.getVersion().validatesRefSiblings();
    }
}
