package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.*;
import java.util.stream.Collectors;

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

    public String getOpenapi () {
        return node.getString (OPENAPI);
    }

    public Info getInfo () {
        return new Info(context, node.getChildNode (INFO));
    }

    public Collection<Server> getServers () {
        return node.getChildNodes (SERVERS)
            .stream ()
            .map (server -> new Server(context, server))
            .collect(Collectors.toList());
    }

    public Paths getPaths () {
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
