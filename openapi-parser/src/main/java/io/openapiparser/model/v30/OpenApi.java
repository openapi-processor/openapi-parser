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
        return node.getRequiredPropertyAsString (OPENAPI);
    }

    @Required
    public Info getInfo () {
        return node.getRequiredPropertyAs (INFO, node -> new Info (context, node));
    }

    @Nullable
    public Collection<Server> getServers () {
        return node.getPropertyAsArrayOf (SERVERS, node -> new Server(context, node));
    }

    @Required
    public Paths getPaths () {
        return node.getRequiredPropertyAs (PATHS, node -> new Paths (context, node));
    }

    @Nullable
    public Components getComponents () {
        return null;
    }

    @Nullable
    public Collection<SecurityRequirement> getSecurity () {
        return null;
    }

    @Nullable
    public Collection<Tag> getTags () {
        return null;
    }

    @Nullable
    public ExternalDocumentation getExternalDocs () {
        return null;
    }
}
