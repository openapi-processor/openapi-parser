package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.converter.Types.convertOrNull;

public class BooleanConverter implements PropertyConverter<Boolean> {
    private final JsonPointer parent;

    public BooleanConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Boolean convert (String name, Object value) {
        return convertOrNull (parent.append (name).toString (), value, Boolean.class);
    }
}
