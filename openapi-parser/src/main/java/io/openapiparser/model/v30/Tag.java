/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;

import java.util.Map;

/**
 * the <em>Tag</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#tag-object">4.7.22 Tag Object</a>
 */
public class Tag extends Properties implements Extensions {

    public Tag (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
