/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiprocessor.jsonschema.converter.PropertiesConverter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * get a map of the extension properties.
 */
public class ExtensionsConverter implements PropertiesConverter<Map<String, @Nullable Object>> {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^x-");

    @Override
    public @Nullable Map<String, @Nullable Object> convert (Map<String, @Nullable Object> properties, String location) {
        Map<String, @Nullable Object> extensions = new LinkedHashMap<> ();

        properties.forEach ((property, value) -> {
            if (isExtension (property)) {
                extensions.put (property, value);
            }
        });

        return Collections.unmodifiableMap (extensions);
    }

    private boolean isExtension (String property) {
        return EXTENSION_PATTERN.matcher(property).find ();
    }
}
