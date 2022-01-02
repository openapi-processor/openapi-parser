/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Server</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#server-object">4.8.5 Server Object</a>
 */
public class Server extends Properties implements Extensions {

    public Server (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getUrl () {
        return getStringOrThrow (URL);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public Map<String, ServerVariable> getVariables () {
        return getMapObjectsOrEmpty (VARIABLES, ServerVariable.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
