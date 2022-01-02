/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Parameter</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#parameter-object">4.7.12 Parameter Object</a>
 */
public class Parameter extends Properties implements Reference, Extensions {

    public Parameter (Context context, Bucket bucket) {
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

    public Parameter getRefObject () {
        return getRefObjectOrThrow (Parameter.class);
    }

    @Required
    public String getName () {
        return getStringOrThrow (NAME);
    }

    @Required
    public String getIn () {
        return getStringOrThrow (IN);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public Boolean getRequired () {
        return getBooleanOrDefault (REQUIRED, false);
    }

    public Boolean getDeprecated () {
        return getBooleanOrDefault (DEPRECATED, false);
    }

    public Boolean getAllowEmptyValue () {
        return getBooleanOrDefault (ALLOW_EMPTY_VALUE, false);
    }

    public String getStyle () {
        String style = getStringOrNull (STYLE);
        if (style != null) {
            return style;
        }

        final String in = getIn ();
        switch (in) {
            case "query":
            case "cookie":
                return "form";
            default:
                return "simple";
        }
    }

    public Boolean getExplode () {
        Boolean explode = getBooleanOrNull (EXPLODE);
        if (explode != null) {
            return explode;
        }

        final String style = getStyle ();
        return "form".equals (style);
    }

    public Boolean getAllowReserved () {
        return getBooleanOrDefault (ALLOW_RESERVED, false);
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
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
