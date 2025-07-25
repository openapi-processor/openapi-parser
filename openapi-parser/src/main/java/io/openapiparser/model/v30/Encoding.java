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

import static io.openapiparser.Keywords.*;

/**
 * the <em>Encoding</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#encoding-object">4.7.15 Encoding Object</a>
 */
public class Encoding extends Properties implements Extensions {

    public Encoding (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public @Nullable String getContentType () {
        return getStringOrNull (CONTENT_TYPE);
    }

    public Map<String, Header> getHeaders () {
        return getMapObjectsOrEmpty (HEADERS, Header.class);
    }

    public String getStyle () {
        String style = getStringOrNull (STYLE);
        if (style != null) {
            return style;
        }

        return "form";
    }

    public Boolean getExplode () {
        Boolean explode = getBooleanOrNull (EXPLODE);
        if (explode != null) {
            return explode;
        }

        final String style = getStyle ();
        return "form".equals (style);
    }

    public Boolean getAllowReserved () {
        return getBooleanOrDefault (ALLOW_RESERVED, false);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
