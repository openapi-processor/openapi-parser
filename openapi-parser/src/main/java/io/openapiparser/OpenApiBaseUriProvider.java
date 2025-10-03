/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.JsonBaseUriProvider;
import io.openapiprocessor.jsonschema.schema.SchemaVersion;
import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

/**
 * OpenAPI 3.2 base uri provider (i.e. $self).
 */
public class OpenApiBaseUriProvider extends JsonBaseUriProvider {
    @Override
    public @Nullable URI get(URI documentUri, Object document, SchemaVersion version) {
        @Nullable Map<String, @Nullable Object> object = Types.asObjectOrNull(document);
        if (object == null) {
            return null;
        }

        Object self = object.get(Keywords.SELF);
        if (Types.isString(self)) {
            return Uris.createUri(Types.asString(self));
        }

        return super.get(documentUri, document, version);
    }
}
