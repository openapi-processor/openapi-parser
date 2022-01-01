/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.converter.*;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Callback</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#callback-object">4.7.18 Callback Object</a>
 */
public class Callback implements Extensions {
    private final Context context;
    private final PropertyBucket properties;

    @Deprecated
    public Callback (Context context, Node node) {
        this.context = context;
        this.properties = null;
    }
    public Callback (Context context, PropertyBucket properties) {
        this.context = context;
        this.properties = properties;
    }

    public Map<String, PathItem> getPathItems() {
        return properties.convert (new ObjectMapConverter<> (context, PathItem.class));
    }

    public @Nullable PathItem getPathItem(String path) {
        return properties.convert (path, new ObjectConverter<> (context, PathItem.class));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return properties.convert (new ExtensionsConverter ());
    }
}
