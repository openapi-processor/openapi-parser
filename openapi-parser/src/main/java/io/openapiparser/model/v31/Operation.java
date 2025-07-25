/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Operation</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#operation-object">4.8.10 Operation Object</a>
 */
public class Operation extends Properties implements Extensions {

    public Operation (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public Collection<String> getTags () {
        return getStringsOrEmpty (TAGS);
    }

    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public @Nullable ExternalDocumentation getExternalDocs () {
        return getObjectOrNull (EXTERNAL_DOCS, ExternalDocumentation.class);
    }

    public @Nullable String getOperationId () {
        return getStringOrNull (OPERATION_ID);
    }

    // todo include pathItem parameter
    public Collection<Parameter> getParameters () {
        return getObjectsOrEmpty (PARAMETERS, Parameter.class);
    }

    public @Nullable RequestBody getRequestBody () {
        return getObjectOrNull (REQUEST_BODY, RequestBody.class);
    }

    public @Nullable Responses getResponses () {
        return getObjectOrNull (RESPONSES, Responses.class);
    }

    public Map<String, Callback> getCallbacks () {
        return getMapObjectsOrEmpty (CALLBACKS, Callback.class);
    }

    public Boolean getDeprecated () {
        return getBooleanOrFalse (DEPRECATED);
    }

    public Collection<SecurityRequirement> getSecurity () {
        return getObjectsOrEmpty (SECURITY, SecurityRequirement.class);
    }

    public Collection<Server> getServers () {
        return getObjectsOrEmpty (SERVERS, Server.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
