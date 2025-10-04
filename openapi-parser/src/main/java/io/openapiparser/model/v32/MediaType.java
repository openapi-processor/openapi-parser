/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Media Type</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#media-type-object">4.8.14 Media Type Object</a>
 */
public class MediaType extends Properties implements Extensions {

    public MediaType (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public @Nullable Schema getSchema () {
        return getObjectOrNull (SCHEMA, Schema.class);
    }

    public @Nullable Schema getItemSchema () {
        return getObjectOrNull (ITEM_SCHEMA, Schema.class);
    }

    public @Nullable Object getExample () {
        return getRawValue (EXAMPLE);
    }

    public Map<String, Example> getExamples () {
        return getMapObjectsOrEmpty (EXAMPLES, Example.class);
    }

    public Map<String, Encoding> getEncoding () {
        return getMapObjectsOrEmpty (ENCODING, Encoding.class);
    }

    public Collection<Encoding> getPrefixEncoding () {
        return getObjectsOrEmpty (PREFIX_ENCODING, Encoding.class);
    }

    public @Nullable Encoding getItemEncoding () {
        return getObjectOrNull (ITEM_ENCODING, Encoding.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
