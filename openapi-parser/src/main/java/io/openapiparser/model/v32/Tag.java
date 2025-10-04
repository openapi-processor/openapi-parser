/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.model.v31.ExternalDocumentation;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Tag</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#tag-object">4.8.22 Tag Object</a>
 */
public class Tag extends Properties implements Extensions {

    public Tag (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getName() {
        return getStringOrThrow(NAME);
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

    public @Nullable String getParent () {
        return getStringOrNull (PARENT);
    }

    public @Nullable String getKind () {
        return getStringOrNull (KIND);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
