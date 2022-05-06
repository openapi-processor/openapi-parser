/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.converter.Types.convertOrNull;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * get a collection of {@link JsonSchema}s.
 */
public class JsonSchemasConverter implements PropertyConverter<Collection<JsonSchema>> {
    private final JsonSchemaContext parentContext;

    public JsonSchemasConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable Collection<JsonSchema> convert (String name, Object value, String location) {
        Collection<?> objects = convertOrNull (location, value, Collection.class);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        int index = 0;
        Collection<JsonSchema> result = new ArrayList<> ();
        for (Object item : objects) {
            result.add (nonNull(create (name, item, getLocation(parentLocation, index++))));
        }
        return Collections.unmodifiableCollection (result);
    }

    private @Nullable JsonSchema create (String name, Object value, String location) {
        return new JsonSchemaConverter (parentContext).convert (name, value, location);
    }

    private String getLocation (JsonPointer parent, int index) {
        return parent.getJsonPointer (String.valueOf (index));
    }
}
