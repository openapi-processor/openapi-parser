/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.openapiparser.*;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link Converter} based on jackson.
 */
public class JacksonConverter implements Converter {
    private static final String CONVERT_ERROR = "failed to convert %s document.";
    private static final Pattern JSON_PATTERN = Pattern.compile("^\\s*\\{");

    private static final ObjectMapper json = new ObjectMapper();
    private static final ObjectMapper yaml = new ObjectMapper(new YAMLFactory ());

    @Override
    public Map<String, Object> convert (String api) throws ConverterException {
        if (isEmpty (api)) {
            throw new ConverterException (String.format (CONVERT_ERROR, "empty"));
        }

        if (isJson (api)) {
            return convertJson (api);
        } else {
            return convertYaml (api);
        }
    }

    private Map<String, Object> convertJson (String api) throws ConverterException {
        try {
            return json.readValue (api, getMapTypeReference ());
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "json"), e);
        }
    }

    private Map<String, Object> convertYaml (String api) throws ConverterException {
        try {
            return yaml.readValue (api, getMapTypeReference ());
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "yaml"), e);
        }
    }

    private TypeReference<Map<String, Object>> getMapTypeReference () {
        return new TypeReference<Map<String, Object>> () {};
    }

    private boolean isJson (String source) {
        return JSON_PATTERN.matcher(source).find ();
    }

    private static boolean isEmpty(String source) {
        return source == null || source.isEmpty ();
    }
}
