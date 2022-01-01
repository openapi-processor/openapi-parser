/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30.converter;

import io.openapiparser.Context;
import io.openapiparser.converter.PropertyBucketConverter;
import io.openapiparser.converter.PropertyConverter;
import io.openapiparser.model.v30.PathItem;
import io.openapiparser.schema.PropertyBucket;

/**
 *  get a path item object from the property.
 */
public class PathItemConverter implements PropertyConverter<PathItem> {
    private final Context context;

    public PathItemConverter (Context context) {
        this.context = context;
    }

    @Override
    public PathItem convert (String name, Object value, String location) {
        PropertyBucket bucket = new PropertyBucketConverter ().convert (name, value, location);
        if (bucket == null)
            return null;

        return new PathItem (context, bucket);
    }
}
