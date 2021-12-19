/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Server</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#server-object">4.7.5 Server Object</a>
 */
public class Server implements Extensions {
    private final Context context;
    private final Node node;

    public Server (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Required
    public String getUrl () {
        return node.getRequiredStringValue (URL);
    }

    public @Nullable String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    public Map<String, ServerVariable> getVariables () {
        return node.getMapObjectValuesOrEmpty (VARIABLES, node -> new ServerVariable (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
