/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import static io.openapiparser.converter.Types.convertNotNull;

/**
 * converts the property {@code value} to {@link String} and throws {@link NoValueException} if the
 * value is {@code null}.
 */
public class StringNotNullConverter implements PropertyConverter<String> {

    @Override
    public String convert (String name, Object value, String location) {
        return convertNotNull (location, value, String.class);
    }
}
