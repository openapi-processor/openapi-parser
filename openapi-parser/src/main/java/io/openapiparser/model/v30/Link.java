/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Link</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.4.html#link-object">4.7.20 Link Object</a>
 */
public class Link extends Properties implements Extensions {

    public Link (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
