/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.ConverterException;

import java.util.regex.Pattern;

/**
 * {@link Converter} based on jackson.
 *
 * Supports json and yaml.
 */
public class JacksonConverter implements Converter {
    private static final String CONVERT_ERROR = "failed to convert %s document.";
    private static final Pattern JSON_PATTERN = Pattern.compile("^\\s*\\{");

    private final ObjectMapper json;
    private final ObjectMapper yaml;

    public JacksonConverter () {
        json = JsonMapper.builder ()
            .disable (MapperFeature.USE_ANNOTATIONS)
            .build ();

        yaml = YAMLMapper.builder (new YAMLFactory ())
            .disable (MapperFeature.USE_ANNOTATIONS)
            .build ();
    }

    @Override
    public Object convert (String api) throws ConverterException {
        if (isEmpty (api)) {
            throw new ConverterException (String.format (CONVERT_ERROR, "empty"));
        }

        if (isJson (api)) {
            return convertJson (api);
        } else {
            return convertYaml (api);
        }
    }

    private Object convertJson (String api) throws ConverterException {
        try {
            return json.readValue (api, Object.class);
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "json"), e);
        }
    }

    private Object convertYaml (String api) throws ConverterException {
        try {
            return yaml.readValue (api, Object.class);
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "yaml"), e);
        }
    }

    private boolean isJson (String source) {
        return JSON_PATTERN.matcher(source).find ();
    }

    private static boolean isEmpty(String source) {
        return source == null || source.isEmpty ();
    }
}
