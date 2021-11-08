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
        return node.getRequiredObjectValue (INFO, node -> new Info (context, node));
    }

    @Nullable
    public String getJsonSchemaDialect () {
        return node.getStringValue (JSON_SCHEMA_DIALECT);
    }

    @Nullable
    public Collection<Server> getServers () {
        return node.getArrayValuesOrEmpty (SERVERS, node -> new Server (context, node));
    }

    @Nullable // @Required (if webhooks or components are null)
    public Paths getPaths () {
        return node.getObjectValue (PATHS, node -> new Paths (context, node));
    }

    @Nullable // @Required (if paths or components are null)
    public Map<String, PathItem> getWebhooks () {
        return node.getObjectValues (WEBHOOKS, node -> new PathItem (context, node));
    }

    @Nullable  // @Required (if paths or webhooks are null)
    public Components getComponents () {
        return node.getObjectValue (COMPONENTS, node -> new Components (context, node));
    }

    @Nullable
    public Collection<SecurityRequirement> getSecurity () {
        return node.getArrayValuesOrEmpty (SECURITY, node -> new SecurityRequirement (context, node));
    }

    @Nullable
    public Collection<Tag> getTags () {
        return node.getArrayValuesOrEmpty (TAGS, node -> new Tag (context, node));
    }

    @Nullable
    public ExternalDocumentation getExternalDocs () {
        return node.getObjectValue (EXTERNAL_DOCS, node -> new ExternalDocumentation (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
