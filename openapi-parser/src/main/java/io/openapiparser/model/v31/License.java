/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>License</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#license-object">4.8.4 License Object</a>
 */
public class License extends Properties implements Extensions {

    public License (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getName () {
        return getStringOrThrow (NAME);
    }

    public @Nullable String getIdentifier () {
        return getStringOrNull (IDENTIFIER);
    }

    public @Nullable String getUrl () {
        return getStringOrNull (URL);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
