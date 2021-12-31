package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;

import static io.openapiparser.converter.Types.convertNotNull;

public class StringConverterRequired implements TypeConverter<String> {
    private final JsonPointer parent;

    public StringConverterRequired (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public String convert (String name, Object value) {
        return convertNotNull (parent.append (name).toString (), value, String.class);
    }
}
