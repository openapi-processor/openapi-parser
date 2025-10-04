/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>OAuth Flow</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#ouath-flow-object">4.29 Oauth Flow Object</a>
 */
public class OAuthFlow extends Properties implements Extensions {

    public OAuthFlow(Context context, Bucket bucket) {
        super(context, bucket);
    }

    @Required
    public String getAuthorizationUrl() {
        return getStringOrThrow(AUTHORIZATION_URL);
    }

    @Required
    public String getDeviceAuthorizationUrl() {
        return getStringOrThrow(DEVICE_AUTHORIZATION_URL);
    }

    @Required
    public String getTokenUrl() {
        return getStringOrThrow(TOKEN_URL);
    }

    public @Nullable String getRefreshUrl() {
        return getStringOrThrow(REFRESH_URL);
    }

    @Required
    public Map<String, String> getScopes() {
        return getMapStringsOrEmpty(SCOPES);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
