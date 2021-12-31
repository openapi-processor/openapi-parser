package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class JsonSchemaConverter implements PropertyConverter<JsonSchema> {
    private final JsonPointer parent;

    public JsonSchemaConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable JsonSchema convert (String name, Object value) {
        if (value == null)
            return null;

        JsonPointer property = parent.append (name);

        if (value instanceof Boolean) {
            return new JsonSchemaBoolean (property, (Boolean) value);

        } else if (value instanceof Map) {
            //noinspection unchecked
            return new JsonSchemaObject (property, (Map<String, Object>) value);
        } else {
            throw new TypeMismatchException (property.toString (), JsonSchema.class);
        }
    }
}
