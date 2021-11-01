/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>OpenAPI</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#openapi-object">4.7.1 OpenAPI Object</a>
 */
public class OpenApi implements Extensions {
    private final Context context;
    private final Node node;

    public OpenApi (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getOpenapi () {
        return node.getPropertyAsString (OPENAPI);
    }

    public Info getInfo () {
        return new Info (context, node.getPropertyAsNode (INFO));
    }

    public String getJsonSchemaDialect () {
        return null;
    }

    public Collection<Server> getServers () {
        return null;
    }

    public Map<String, PathItem> getPaths () {
        return null;
    }

    public Map<String, PathItem> getWebhooks () {
        return null;
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
