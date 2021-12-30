package io.openapiparser.converter;

import io.openapiparser.schema.Content;
import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class ContentConverter implements TypeConverter<Content> {
    private final JsonPointer parent;

    public ContentConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Content convert (String name, Object value) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (parent.append (name).toString (), Map.class);

        //noinspection unchecked
        return new Content ((Map<String, Object>) value);
    }
}