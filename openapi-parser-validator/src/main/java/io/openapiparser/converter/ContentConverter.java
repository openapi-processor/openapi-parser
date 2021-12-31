package io.openapiparser.converter;

import io.openapiparser.schema.PropertiesBucket;
import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

// todo name
public class ContentConverter implements PropertyConverter<PropertiesBucket> {
    private final JsonPointer parent;

    public ContentConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable PropertiesBucket convert (String name, Object value) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (parent.append (name).toString (), Map.class);

        //noinspection unchecked
        return new PropertiesBucket ((Map<String, Object>) value);
    }
}
