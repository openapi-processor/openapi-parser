/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Paths</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#paths-object">4.8.8 Paths Object</a>
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
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
