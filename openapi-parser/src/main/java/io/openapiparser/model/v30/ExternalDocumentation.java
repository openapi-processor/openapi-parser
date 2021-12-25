/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.DESCRIPTION;
import static io.openapiparser.Keywords.URL;

/**
 * the <em>External Documentation</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#external-documentation-object">
 *  4.7.11 External Documentation Object
 * </a>
 */
public class ExternalDocumentation implements Extensions {
    private final Context context;
    private final Node node;

    public ExternalDocumentation (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public @Nullable String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    @Required
    public String getUrl () {
        return node.getRequiredStringValue (URL);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
