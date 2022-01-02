/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import static io.openapiparser.converter.Types.convertNotNull;

/**
 * converts property {@code value} to {@link String} object and throws {@link NoValueException} if
 * the value does not exist.
 */
public class StringNotNullConverter implements PropertyConverter<String> {

    @Override
    public String convert (String name, Object value, String location) {
        return convertNotNull (location, value, String.class);
    }
}
