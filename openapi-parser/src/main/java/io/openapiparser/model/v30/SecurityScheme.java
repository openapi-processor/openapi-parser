/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.schema.Bucket;

import java.util.Map;

/**
 * the <em>Security Scheme</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#security-scheme-object">
 *   4.7.28 Security Scheme Object
 * </a>
 */
public class SecurityScheme implements Extensions {
    private final Context context;
    private final Bucket properties;

    public SecurityScheme (Context context, Bucket properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }
}
