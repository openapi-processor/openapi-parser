/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Example</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#example-object">4.8.19 Example Object</a>
 */
public class Example extends Properties implements Reference, Extensions {

    public Example (Context context, Bucket bucket) {
        super (context, bucket);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    /** {@inheritDoc} */
    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    @Override
    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    @Override
    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public @Nullable Object getValue () {
        return getRawValue (VALUE);
    }

    public @Nullable String getExternalValue () {
        return getStringOrNull (EXTERNAL_VALUE);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
