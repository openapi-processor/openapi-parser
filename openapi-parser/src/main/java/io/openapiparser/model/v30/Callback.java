/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Callback</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#callback-object">4.7.18 Callback Object</a>
 */
public class Callback extends Properties implements Extensions  {

    public Callback (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public Map<String, PathItem> getPathItems() {
        return getMapObjectsOrEmpty (PathItem.class);
    }

    public @Nullable PathItem getPathItem(String path) {
        return getObjectOrNull (path, PathItem.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
