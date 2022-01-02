/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Contact</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#contact-object">4.7.3 Contact Object</a>
 */
public class Contact extends Properties implements Extensions {

    public Contact (Context context, Bucket bucket) {
        super (context, bucket);
    }

    public @Nullable String getName () {
        return getStringOrNull (NAME);
    }

    public @Nullable String getUrl () {
        return getStringOrNull (URL);
    }

    public @Nullable String getEmail () {
        return getStringOrNull (EMAIL);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
