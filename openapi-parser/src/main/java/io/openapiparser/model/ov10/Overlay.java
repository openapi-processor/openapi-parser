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

import static io.openapiparser.Keywords.OVERLAY;

/**
 * the <em>Overlay</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/overlay/v1.0.0.html#overlay-object">4.4.1 Overlay Object</a>
 */
public class Overlay extends Properties implements Extensions {

    public Overlay (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getOverlay () {
        return getStringOrThrow (OVERLAY);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return super.getExtensions ();
    }
}
