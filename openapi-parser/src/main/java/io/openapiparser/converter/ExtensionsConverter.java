/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import java.util.*;
import java.util.regex.Pattern;

/**
 * get a map of the extension properties.
 */
public class ExtensionsConverter implements PropertiesConverter<Map<String, Object>> {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^x-");

    @Override
    public Map<String, Object> convert (Map<String, Object> properties, String location) {
        Map<String, Object> extensions = new LinkedHashMap<> ();

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
