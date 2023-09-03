/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.JsonSchemaContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static io.openapiprocessor.jsonschema.support.Types.convertOrNull;
import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * get a collection of {@link JsonSchema}s.
 */
public class JsonSchemasConverter implements PropertyConverter<Collection<JsonSchema>> {
    private final JsonSchemaContext parentContext;

    public JsonSchemasConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable Collection<JsonSchema> convert (String name, @Nullable Object value, String location) {
        Collection<@NonNull ?> objects = convertOrNull (location, value, Collection.class);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        int index = 0;
        Collection<JsonSchema> result = new ArrayList<> ();
        for (Object item : objects) {
            result.add (create (name, item, getLocation(parentLocation, index++)));
        }
        return Collections.unmodifiableCollection (result);
    }

    private JsonSchema create (String name, Object value, String location) {
        return nonNull(new JsonSchemaConverter (parentContext).convert (name, value, location));
    }

    private String getLocation (JsonPointer parent, int index) {
        return parent.getJsonPointer (String.valueOf (index));
    }
}
