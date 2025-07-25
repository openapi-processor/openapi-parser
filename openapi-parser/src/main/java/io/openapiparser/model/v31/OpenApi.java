/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>OpenAPI</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#openapi-object">4.7.1 OpenAPI Object</a>
 */
public class OpenApi extends Properties implements Extensions {

    public OpenApi (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getOpenapi () {
        return getStringOrThrow (OPENAPI);
    }

    @Required
    public Info getInfo () {
        return getObjectOrThrow (INFO, Info.class);
    }

    public @Nullable String getJsonSchemaDialect () {
        return getStringOrNull (JSON_SCHEMA_DIALECT);
    }

    public Collection<Server> getServers () {
        return getObjectsOrEmpty (SERVERS, Server.class);
    }

    // @Required (if webhooks or components are null)
    public @Nullable Paths getPaths () {
        return getObjectOrNull (PATHS, Paths.class);
    }

     // @Required (if paths or components are null)
    public Map<String, PathItem> getWebhooks () {
        return getMapObjectsOrEmpty (WEBHOOKS, PathItem.class);
    }

    // @Required (if paths or webhooks are null)
    public @Nullable Components getComponents () {
        return getObjectOrNull (COMPONENTS, Components.class);
    }

    public Collection<SecurityRequirement> getSecurity () {
        return getObjectsOrEmpty (SECURITY, SecurityRequirement.class);
    }

    public Collection<Tag> getTags () {
        return getObjectsOrEmpty (TAGS, Tag.class);
    }

    public @Nullable ExternalDocumentation getExternalDocs () {
        return getObjectOrNull (EXTERNAL_DOCS, ExternalDocumentation.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
