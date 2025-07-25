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
 * the <em>Header</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#header-object">4.8.21 Header Object</a>
 */
public class Header extends Properties implements Reference, Extensions {

    public Header (Context context, Bucket bucket) {
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

    public Boolean getRequired () {
        return getBooleanOrFalse (REQUIRED);
    }

    public Boolean getDeprecated () {
        return getBooleanOrFalse (DEPRECATED);
    }

    public Boolean getAllowEmptyValue () {
        return getBooleanOrFalse (ALLOW_EMPTY_VALUE);
    }

    public String getStyle () {
        String style = getStringOrNull (STYLE);
        if (style != null) {
            return style;
        }

        return "simple";
    }

    public Boolean getExplode () {
        Boolean explode = getBooleanOrNull (EXPLODE);
        if (explode != null) {
            return explode;
        }

        return false;
    }

    public Boolean getAllowReserved () {
        return getBooleanOrFalse (ALLOW_RESERVED);
    }

    public @Nullable Schema getSchema () {
        return getObjectOrNull (SCHEMA, Schema.class);
    }

    public @Nullable Object getExample () {
        return getRawValue (EXAMPLE);
    }

    public Map<String, Example> getExamples () {
        return getMapObjectsOrEmpty (EXAMPLES, Example.class);
    }

    public Map<String, MediaType> getContent () {
        return getMapObjectsOrEmpty (CONTENT, MediaType.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
