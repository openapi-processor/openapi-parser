package io.openapiparser.validator.converter;

import io.openapiparser.validator.JsonPointer;

import static io.openapiparser.validator.converter.Types.convertNotNull;

public class StringConvertRequired implements TypeConverter<String> {
    private final JsonPointer parent;

    public StringConvertRequired (JsonPointer parent) {
        this.parent = parent;
    }

    @Override
    public String convert (String name, Object value) {
        return convertNotNull (parent.append (name).toString (), value, String.class);
    }
}
