/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * the <em>Example</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#example-object">4.8.19 Example Object</a>
 */
public class Example implements Reference {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Example (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = null; //getRefNode ();
    }

    @Override
    public boolean isRef () {
        return false;
    }

    @Override
    @Required
    public String getRef () {
        return null;
    }

    @Override
    public @Nullable String getSummary () {
        return null;
    }

    @Override
    public @Nullable String getDescription () {
        return null;
    }
}
