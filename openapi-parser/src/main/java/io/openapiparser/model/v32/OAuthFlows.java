/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>OAuth Flows</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#oauth-flows-object">4.28 Oauth Flows Object</a>
 */
public class OAuthFlows extends Properties implements Extensions {

    public OAuthFlows(Context context, Bucket bucket) {
        super(context, bucket);
    }

    public @Nullable OAuthFlow getImplicit() {
        return getObjectOrNull(IMPLICIT, OAuthFlow.class);
    }

    public @Nullable OAuthFlow getPassword() {
        return getObjectOrNull(PASSWORD, OAuthFlow.class);
    }

    public @Nullable OAuthFlow getClientCredentials() {
        return getObjectOrNull(CLIENT_CREDENTIALS, OAuthFlow.class);
    }

    public @Nullable OAuthFlow getAuthorizationCode() {
        return getObjectOrNull(AUTHORIZATION_CODE, OAuthFlow.class);
    }

    public @Nullable OAuthFlow getDeviceAuthorization() {
        return getObjectOrNull(DEVICE_AUTHORIZATION, OAuthFlow.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
