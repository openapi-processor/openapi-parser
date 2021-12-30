/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.PropertiesConverter;
import io.openapiparser.converter.TypeConverter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * .. todo
 */
public class Properties {
    // JsonPointer ???
    private final Map<String, Object> properties;

    public Properties (Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * convert the property value to the expected type.
     *
     * @param property property name
     * @param converter property converter
     * @param <T> target type
     * @return a {@code T} object or null
     */
    public <T> @Nullable T convert (String property, TypeConverter<T> converter) {
        return converter.convert (property, getRawValue (property));
    }

    public <T> @Nullable T convert (PropertiesConverter<T> converter) {
        return converter.convert (properties);
    }

//    @Deprecated
//    public @Nullable Content getContentValue (JsonPointer pointer, String property) {
//        JsonPointer propertyPointer = pointer.append (property);
//        Map<String, Object> raw = getRawMapValue (propertyPointer, property);
//        if (raw == null)
//            return null;
//
//        return new Content (raw);
//    }

    /**
     * get the raw value of the given property.
     *
     * @param property property name
     * @return property value or null if the property does not exist
     */
    private @Nullable Object getRawValue (String property) {
        return properties.get (property);
    }

//    private @Nullable Map<String, Object> getRawMapValue (JsonPointer pointer, String property) {
//        return Types.convertMapOrNull (pointer.toString (), properties.get (property));
//    }
}
