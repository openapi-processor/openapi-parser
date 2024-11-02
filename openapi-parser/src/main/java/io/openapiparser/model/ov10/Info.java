/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;

import java.util.Map;

import static io.openapiparser.Keywords.TITLE;
import static io.openapiparser.Keywords.VERSION;

/**
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/overlay/v1.0.0.html#info-object">4.4.2 Info Object</a>
 */
public class Info extends Properties implements Extensions {

    public Info(Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getTitle () {
        return getStringOrThrow (TITLE);
    }

    @Required
    public String getVersion () {
        return getStringOrThrow (VERSION);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
