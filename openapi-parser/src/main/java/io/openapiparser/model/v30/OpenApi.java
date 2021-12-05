/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

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
        return node.getRequiredStringValue (OPENAPI);
    }

    @Required
    public Info getInfo () {
        return node.getRequiredObjectValue (INFO, node -> new Info (context, node));
    }

    public Collection<Server> getServers () {
        return node.getArrayValuesOrEmpty (SERVERS, node -> new Server(context, node));
    }

    @Required
    public Paths getPaths () {
        return node.getRequiredObjectValue (PATHS, node -> new Paths (context, node));
    }

    public @Nullable Components getComponents () {
        return node.getObjectValue (COMPONENTS, node -> new Components (context, node));
    }

    public Collection<SecurityRequirement> getSecurity () {
        return node.getArrayValuesOrEmpty (SECURITY, node -> new SecurityRequirement (context, node));
    }

    public Collection<Tag> getTags () {
        return node.getArrayValuesOrEmpty (TAGS, node -> new Tag (context, node));
    }

    public @Nullable ExternalDocumentation getExternalDocs () {
        return node.getObjectValue (EXTERNAL_DOCS, node -> new ExternalDocumentation (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
