package io.openapiparser.model.v30;

import io.openapiparser.Context;

import java.util.Collection;
import java.util.Map;

/**
 * the <em>OpenAPI</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#openapi-object">4.7.1 OpenAPI Object</a>
 */
public class OpenApi implements Extensions {
    private final Context context;

    public OpenApi (Context context) {
        this.context = context;
    }

    public String getOpenapi () {
        return null;
    }

    public Info getInfo () { return null; }

    public Collection<Server> getServers () {
        return null;
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
