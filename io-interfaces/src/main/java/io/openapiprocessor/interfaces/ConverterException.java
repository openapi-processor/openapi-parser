/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.interfaces;

/**
 * thrown by {@link Converter} if {@link Converter#convert(String)} fails.
 */
public class ConverterException extends Exception {

    public ConverterException (String message) {
        super(message);
    }

    public ConverterException (String message, Exception e) {
        super(message, e);
    }
}
