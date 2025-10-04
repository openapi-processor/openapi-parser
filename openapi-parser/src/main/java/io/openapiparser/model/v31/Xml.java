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
import static io.openapiparser.Keywords.ATTRIBUTE;
import static io.openapiparser.Keywords.WRAPPED;

/**
 * the <em>XML</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#xml-object">4.8.26 XML Object</a>
 */
public class Xml extends Properties implements Extensions {

    public Xml (Context context, Bucket bucket) {
        super (context, bucket);
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

    public Boolean getAttribute () {
        return getBooleanOrFalse (ATTRIBUTE);
    }

    public Boolean getWrapped () {
        return getBooleanOrFalse (WRAPPED);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
