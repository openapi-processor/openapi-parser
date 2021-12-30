package io.openapiparser.validator.converter;

import io.openapiparser.validator.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.validator.converter.Types.convertOrNull;

public class StringConverter implements TypeConverter<String> {
    private final JsonPointer parent;

    public StringConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable String convert (String name, Object value) {
        return convertOrNull (parent.append (name).toString (), value, String.class);
    }
}
