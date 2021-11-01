/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

import java.util.*;

import static io.openapiparser.Keywords.*;

/**
 * the <em>OpenAPI</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#openapi-object">4.7.1 OpenAPI Object</a>
 */
public class OpenApi implements Extensions {
    private final Context context;
    private final Node node;

    public OpenApi (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Required
    public String getOpenapi () {
        return node.getRequiredAsString (OPENAPI);
    }

    @Required
    public Info getInfo () {
        return node.getRequiredChild (INFO, node -> new Info (context, node));
    }

    public Collection<Server> getServers () {
        return node.getChildArrayAs (SERVERS, node -> new Server(context, node));
    }

    @Required
    public Paths getPaths () {
        return node.getChildAs (PATHS, node -> new Paths (context, node));
    }

    public Components getComponents () {
        return null;
    }

    public Collection<SecurityRequirement> getSecurity () {
        return null;
    }

    public Collection<Tag> getTags () {
        return null;
    }

    public ExternalDocumentation getExternalDocs () {
        return null;
    }
}
