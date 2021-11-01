/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

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

    public String getUrl () {
        return node.getAsString (URL);
    }

    public String getDescription () {
        return node.getAsString (DESCRIPTION);
    }

    public Map<String, ServerVariable> getVariables () {
        return node.getChildMapAs (VARIABLES, node -> new ServerVariable (context, node));
    }
}
