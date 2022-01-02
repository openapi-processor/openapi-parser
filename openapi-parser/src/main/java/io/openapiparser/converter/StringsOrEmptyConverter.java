/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import java.util.*;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * get a collection of {@link String}s.
 */
public class StringsOrEmptyConverter implements PropertyConverter<Collection<String>> {

    @Override
    public Collection<String> convert (String name, Object value, String location) {
        Collection<?> values = convertOrNull (location, value, Collection.class);
        if (values == null)
            return Collections.emptyList ();

        return Collections.unmodifiableCollection (asStrings (values));
    }

    @SuppressWarnings ("unchecked")
    private Collection<String> asStrings(Collection<?> values) {
        return (Collection<String>)values;
    }
}
