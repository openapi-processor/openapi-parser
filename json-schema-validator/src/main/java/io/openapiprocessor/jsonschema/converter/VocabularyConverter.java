/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.JsonSchemaContext;
import io.openapiprocessor.jsonschema.schema.Vocabularies;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Types.asBoolean;
import static io.openapiprocessor.jsonschema.support.Types.convertMapOrNull;
import static io.openapiprocessor.jsonschema.support.Uris.createUri;

public class VocabularyConverter implements PropertyConverter<Vocabularies> {
    private final JsonSchemaContext context;

    public VocabularyConverter (JsonSchemaContext context) {
        this.context = context;
    }

    @Override
    public @Nullable Vocabularies convert (String name, @Nullable Object value, String location) {
        Map<String, Object> objects = convertMapOrNull (location, value);
        if (objects == null)
            return null;

        Map<URI, Boolean> vocabularies = new LinkedHashMap<> ();
        objects.forEach ((propKey, propValue) -> {
            vocabularies.put (createUri (propKey), asBoolean (propValue));
        });

        return Vocabularies.create (vocabularies, context.getVersion ());
    }
}
