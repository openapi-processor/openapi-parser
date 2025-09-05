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
 * the <em>Server Variable</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#server-variable-object">4.8.6 Server Variable Object</a>
 */
public class ServerVariable extends Properties implements Extensions {

    public ServerVariable (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public Collection<String> getEnum () {
        return getStringsOrEmpty (ENUM);
    }

    @Required
    public String getDefault () {
        return getStringOrThrow (DEFAULT);
    }

   public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
