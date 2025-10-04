/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>XML</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#xml-object">4.8.26 XML Object</a>
 */
public class Xml extends Properties implements Extensions {

    public Xml (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public @Nullable String getNodeType () {
        return getStringOrNull (NODE_TYPE);
    }

    public @Nullable String getName () {
        return getStringOrNull (NAME);
    }

    public @Nullable String getNamespace () {
        return getStringOrNull (NAMESPACE);
    }

    public @Nullable String getPrefix () {
        return getStringOrNull (PREFIX);
    }

    @Deprecated // Use nodeType: "attribute" instead of attribute: true
    public Boolean getAttribute () {
        return getBooleanOrFalse (ATTRIBUTE);
    }

    @Deprecated // Use nodeType: "element" instead of wrapped: true
    public Boolean getWrapped () {
        return getBooleanOrFalse (WRAPPED);
    }


    public Boolean getAllowReserved () {
        return getBooleanOrFalse (ALLOW_RESERVED);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
