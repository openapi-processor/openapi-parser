package io.openapiparser.validator.converter;

import io.openapiparser.validator.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.validator.converter.Types.convertOrNull;

public class BooleanConverter implements TypeConverter<Boolean> {
    private final JsonPointer parent;

    public BooleanConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Boolean convert (String name, Object value) {
        return convertOrNull (parent.append (name).toString (), value, Boolean.class);
    }
}
