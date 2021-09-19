package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.support.Node;

import java.util.Collection;
import java.util.Map;

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
        return null;
    }

    public Info getInfo () { return null; }

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
