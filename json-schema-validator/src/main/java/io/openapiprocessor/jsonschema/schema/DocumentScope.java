/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

public class DocumentScope {
    private final BaseUriProvider baseUriProvider;

    public DocumentScope(BaseUriProvider baseUriProvider) {
        this.baseUriProvider = baseUriProvider;
    }

    public Scope createScope (URI documentUri, Object document, SchemaVersion fallback) {
        SchemaVersion version = Scope.getSchemaVersion(documentUri, document, fallback);

        if (!Types.isObject (document))
            return new Scope (documentUri, null, version);

        @Nullable URI baseUri = baseUriProvider.get(documentUri, document, version);
        if (baseUri == null) {
            return new Scope (documentUri, null, version);
        }

        return new Scope (documentUri, Uris.resolve(documentUri, baseUri), version);
    }

    public Scope createScope(URI documentUri, Object document, Scope sourceScope) {
        @Nullable URI baseUri = baseUriProvider.get(documentUri, document, sourceScope.getVersion());
        if (baseUri == null) {
            return sourceScope.move (documentUri, document);
        }

        return sourceScope.move (baseUri, document);
    }
}
