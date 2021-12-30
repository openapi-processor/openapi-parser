package io.openapiparser.validator.converter;

import io.openapiparser.validator.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class UriConverter implements TypeConverter<URI> {
    private final JsonPointer parent;

    public UriConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable URI convert (String name, Object value) {
        if (value == null)
            return null;

        if (!(value instanceof String))
            throw new TypeMismatchException (parent.append (name).toString (), String.class);

        return URI.create ((String)value);
    }
}
