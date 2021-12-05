/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

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
        return node.getRequiredStringValue (NAME);
    }

    public @Nullable String getIdentifier () {
        return node.getStringValue (IDENTIFIER);
    }

    public @Nullable String getUrl () {
        return node.getStringValue (URL);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
