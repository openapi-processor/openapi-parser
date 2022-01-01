/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.converter.BooleanConverter;
import io.openapiparser.converter.StringConverter;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Encoding</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#encoding-object">4.7.15 Encoding Object</a>
 */
public class Encoding implements Extensions {
    private final Context context;
    private final Bucket properties;

    public Encoding (Context context, Bucket properties) {
        this.context = context;
        this.properties = properties;
    }

    public @Nullable String getContentType () {
        return getStringOrNull (CONTENT_TYPE);
    }

    public Map<String, Header> getHeaders () {
        return node.getMapObjectValuesOrEmpty (HEADERS, node -> new Header (context, node));
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
        return node.getBooleanValue (ALLOW_RESERVED, false);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private @Nullable String getStringOrNull (String property) {
        return properties.convert (property, new StringConverter ());
    }

    private @Nullable Boolean getBooleanOrNull (String property) {
        return properties.convert (property, new BooleanConverter ());
    }

    private Boolean getBooleanOrDefault (String property, boolean defaultValue) {
        Boolean value = getBooleanOrNull (property);
        if (value == null)
            return defaultValue;

        return value;
    }
}
