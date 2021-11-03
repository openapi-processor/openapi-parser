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

    @Required
    public String getOpenapi () {
        return node.getRequiredStringValue (OPENAPI);
    }

    @Required
    public Info getInfo () {
        return node.getRequiredPropertyAs (INFO, node -> new Info (context, node));
    }

    @Nullable
    public String getJsonSchemaDialect () {
        return null;
    }

    @Nullable
    public Collection<Server> getServers () {
        return null;
    }

    // requires one of  path, webhooks or components
    @Nullable
    public Map<String, PathItem> getPaths () {
        return null;
    }

    // requires one of  path, webhooks or components
    @Nullable
    public Map<String, PathItem> getWebhooks () {
        return null;
    }

    // requires one of  path, webhooks or components
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
