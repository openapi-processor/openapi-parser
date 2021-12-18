/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;
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
    private final Node node;

    public Encoding (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public @Nullable String getContentType () {
        return node.getStringValue (CONTENT_TYPE);
    }

    public Map<String, Header> getHeaders () {
        return node.getObjectValuesOrEmpty (HEADERS, node -> new Header (context, node));
    }

    public String getStyle () {
        String style = node .getStringValue (STYLE);
        if (style != null) {
            return style;
        }

        return "form";
    }

    public Boolean getExplode () {
        Boolean explode = node.getBooleanValue (EXPLODE);
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
}
