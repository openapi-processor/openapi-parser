/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.snakeyaml;

import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.ConverterException;
import org.yaml.snakeyaml.Yaml;

/**
 * {@link Converter} based on snakeyaml.
 *
 * Supports yaml.
 */
public class SnakeYamlConverter implements Converter {
    private static final String CONVERT_ERROR = "failed to convert %s document.";

    @Override
    public Object convert (String api) throws ConverterException {
        if (isEmpty (api)) {
            throw new ConverterException (String.format (CONVERT_ERROR, "empty"));
        }

        return convertYaml (api);
    }

    private Object convertYaml (String api) throws ConverterException {
        try {
            return new Yaml().load (api);
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "yaml"), e);
        }
    }

    private static boolean isEmpty(String source) {
        return source == null || source.isEmpty ();
    }
}
