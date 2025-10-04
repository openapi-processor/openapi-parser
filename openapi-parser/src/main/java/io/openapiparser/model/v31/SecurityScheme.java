/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Security Scheme</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#security-scheme-object">
 *   4.8.28 Security Scheme Object
 * </a>
 */
public class SecurityScheme extends Properties implements Extensions {

    public SecurityScheme (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getType () {
        return getStringOrThrow (TYPE);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    @Required
    public String getName () {
        return getStringOrThrow (NAME);
    }

    @Required
    public String getIn () {
        return getStringOrThrow (IN);
    }

    @Required
    public String getScheme () {
        return getStringOrThrow (SCHEME);
    }

    public @Nullable String getBearerFormat () {
        return getStringOrNull (BEARER_FORMAT);
    }

    public OAuthFlows getFlows () {
        return getObjectOrThrow(FLOWS, OAuthFlows.class);
    }

    @Required
    public String getOpenIdConnectUrl () {
        return getStringOrThrow (OPENID_CONNECT_URL);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
