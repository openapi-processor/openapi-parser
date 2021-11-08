/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;

import java.util.Map;

/**
 * the <em>License</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#license-object">4.8.4 License Object</a>
 */
public class License implements Extensions {
    private final Context context;
    private final Node node;

    public License (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Required
    public String getName () {
        return "todo";
    }

    @Nullable
    public String getIdentifier () {
        return null;
    }

    @Nullable
    public String getUrl () {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }
}
