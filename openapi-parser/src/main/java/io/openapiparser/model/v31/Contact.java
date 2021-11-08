/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;

import java.util.Map;

/**
 * the <em>Contact</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#contact-object">4.8.3 Contact Object</a>
 */
public class Contact implements Extensions {
    private final Context context;
    private final Node node;

    public Contact (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Nullable
    public String getName () {
        return null;
    }

    @Nullable
    public String getUrl () {
        return null;
    }

    @Nullable
    public String getEmail () {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }
}
