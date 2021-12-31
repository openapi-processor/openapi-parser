package io.openapiparser.converter;

import io.openapiparser.schema.PropertyBucket;
import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

// todo name
public class ContentConverter implements PropertyConverter<PropertyBucket> {
    private final JsonPointer parent;

    public ContentConverter (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable PropertyBucket convert (String name, Object value) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (parent.append (name).toString (), Map.class);

        //noinspection unchecked
        return new PropertyBucket ((Map<String, Object>) value);
    }
}
