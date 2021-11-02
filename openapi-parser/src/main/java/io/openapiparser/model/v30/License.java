/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

import static io.openapiparser.Keywords.*;

/**
 * the <em>License</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#license-object">4.7.4 License Object</a>
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
        return node.getRequiredPropertyAsString (NAME);
    }

    public @Nullable String getUrl () {
        return node.getPropertyAsString (URL);
    }
}
