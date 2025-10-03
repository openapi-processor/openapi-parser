/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class JsonBaseUriProvider implements BaseUriProvider {

    @Override
    public @Nullable URI get(URI documentUri, Object document, SchemaVersion version) {
        IdProvider provider = version.getIdProvider();
        String id = provider.getId(Types.asMap(document));
        if (id == null) {
            return null;
        }

        return Uris.createUri(id);
    }
}
