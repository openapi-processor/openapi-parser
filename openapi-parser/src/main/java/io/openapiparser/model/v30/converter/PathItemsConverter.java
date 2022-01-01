/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30.converter;

import io.openapiparser.Context;
import io.openapiparser.converter.NoValueException;
import io.openapiparser.converter.PropertiesConverter;
import io.openapiparser.model.v30.PathItem;
import io.openapiparser.schema.JsonPointer;

import java.util.*;

/**
 * get a map of {@link PathItem}s.
 */
public class PathItemsConverter implements PropertiesConverter<Map<String, PathItem>>  {
    private final Context context;

    public PathItemsConverter (Context context) {
        this.context = context;
    }

    @Override
    public Map<String, PathItem> convert (Map<String, Object> properties, String location) {
        PathItemConverter converter = new PathItemConverter (context);
        Map<String, PathItem> pathItems = new LinkedHashMap<> ();

        properties.forEach ((property, value) -> {
            String propertyLocation = getLocation (location, property);

            PathItem pathItem = converter.convert (property, value, propertyLocation);
            if (pathItem == null) {
                throw new NoValueException (propertyLocation);
            }

            pathItems.put (property, pathItem);
        });

        return Collections.unmodifiableMap (pathItems);
    }

    private String getLocation (String location, String property) {
        return JsonPointer.fromJsonPointer (location).getJsonPointer (property);
    }
}
