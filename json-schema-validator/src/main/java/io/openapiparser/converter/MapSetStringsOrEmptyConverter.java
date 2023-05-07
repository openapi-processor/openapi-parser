/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.converter.Types.*;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

/**
 * get a {@link Map} of {@link Set}&lt;{@link String}&gt; from the {@code name} property
 * {@code value}.
 */
public class MapSetStringsOrEmptyConverter implements PropertyConverter<Map<String, Set<String>>> {

    private final ResponseType responseType;

    public MapSetStringsOrEmptyConverter () {
        responseType = ResponseType.Empty;
    }

    public MapSetStringsOrEmptyConverter (ResponseType responseType) {
        this.responseType = responseType;
    }

    @Override
    public @Nullable Map<String, Set<String>> convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return responseType == ResponseType.Empty ? Collections.emptyMap () : null;

        Map<String, Object> values = convertMap (location, value);

        Map<String, Set<String>> required = new LinkedHashMap<> ();
        values.forEach ((propName, propValue) -> {
            Collection<String> propValues = asStrings (
                convertCollection (getLocation (location, propName), propValue));

            required.put (propName, unmodifiableSet (new LinkedHashSet<> (propValues)));
        });

        return unmodifiableMap (required);
    }

    @SuppressWarnings ("unchecked")
    private Collection<String> asStrings (Collection<?> values) {
        return (Collection<String>) values;
    }

    private String getLocation (String location, String property) {
        return JsonPointer.from (location).getJsonPointer (property);
    }
}
