/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Paths</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#paths-object">4.8.8 Paths Object</a>
 */
public class Paths extends Properties implements Extensions {

    public Paths (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public Map<String, PathItem> getPathItems () {
        return getMapObjectsOrEmpty (PathItem.class);
    }

    public @Nullable PathItem getPathItem (String path) {
        return getObjectOrNull (path, PathItem.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
