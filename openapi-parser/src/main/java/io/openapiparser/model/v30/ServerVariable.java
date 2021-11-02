/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

import java.util.Collection;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Server Variable</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#server-variable-object">4.7.6 Server Variable Object</a>
 */
public class ServerVariable implements Extensions {
    private final Context context;
    private final Node node;

    public ServerVariable (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Nullable
    public Collection<String> getEnum () {
        return node.getPropertyAsStrings (ENUM);
    }

    @Required
    public String getDefault () {
        return node.getRequiredPropertyAsString (DEFAULT);
    }

    @Nullable
    public String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }
}
