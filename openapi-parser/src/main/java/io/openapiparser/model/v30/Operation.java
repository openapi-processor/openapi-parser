/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.converter.MapObjectsOrEmptyFromPropertyConverter;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Operation</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#operation-object">4.7.10 Operation Object</a>
 */
public class Operation implements Extensions {
    private final Context context;
    private final Node node;
    private final Bucket properties;

    public Operation (Context context, Node node) {
        this.context = context;
        this.node = node;
        this.properties = node.toBucket ();
    }

    public Collection<String> getTags () {
        return node.getStringValuesOrEmpty (TAGS);
    }

    public @Nullable String getSummary () {
        return node.getStringValue (SUMMARY);
    }

    public @Nullable String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    public @Nullable ExternalDocumentation getExternalDocs () {
        return node.getObjectValue (EXTERNAL_DOCS, node -> new ExternalDocumentation (context, node));
    }

    public @Nullable String getOperationId () {
        return node.getStringValue (OPERATION_ID);
    }

    // todo include pathItem parameter
    public Collection<Parameter> getParameters () {
        return node.getObjectValuesOrEmpty (PARAMETERS, node -> new Parameter (context, node));
    }

    public @Nullable RequestBody getRequestBody () {
        return node.getObjectValue (REQUEST_BODY, node -> new RequestBody (context, node));
    }

    public @Nullable Responses getResponses () {
        return node.getRequiredObjectValue (RESPONSES, node -> new Responses (context, node));
    }

    public Map<String, Callback> getCallbacks () {
        return getObjectMapFromProperty (CALLBACKS, Callback.class);
    }

    public Boolean getDeprecated () {
        return node.getBooleanValue (DEPRECATED, false);
    }

    public Collection<SecurityRequirement> getSecurity () {
        return node.getObjectValuesOrEmpty (SECURITY, node -> new SecurityRequirement (context, node));
    }

    public Collection<Server> getServers () {
        return node.getObjectValuesOrEmpty (SERVERS, node -> new Server(context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private <T> Map<String, T> getObjectMapFromProperty (String property, Class<T> clazz) {
        return properties.convert (property, new MapObjectsOrEmptyFromPropertyConverter<> (context, clazz));
    }
}
