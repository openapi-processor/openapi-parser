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

import static io.openapiparser.Keywords.DESCRIPTION;
import static io.openapiparser.Keywords.URL;

/**
 * the <em>External Documentation</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#external-documentation-object">
 *  4.8.11 External Documentation Object
 * </a>
 */
public class ExternalDocumentation extends Properties implements Extensions {

    public ExternalDocumentation (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    @Required
    public String getUrl () {
        return getStringOrThrow (URL);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
