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
 * the <em>Contact</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#contact-object">4.7.3 Contact Object</a>
 */
public class Contact implements Extensions {
    private final Context context;
    private final Node node;

    public Contact (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public @Nullable String getName () {
        return node.getStringValue (NAME);
    }

    public @Nullable String getUrl () {
        return node.getStringValue (URL);
    }

    public @Nullable String getEmail () {
        return node.getStringValue (EMAIL);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
