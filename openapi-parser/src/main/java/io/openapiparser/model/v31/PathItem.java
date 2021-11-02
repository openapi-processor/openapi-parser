/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Nullable;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#path-item-object">4.8.9 Path Item Object</a>
 */
public class PathItem implements Extensions, Reference {

    @Nullable
    @Override
    public String getRef () {
        return null;
    }

    @Nullable
    @Override
    public String getSummary () {
        return null;
    }

    @Nullable
    @Override
    public String getDescription () {
        return null;
    }
}
