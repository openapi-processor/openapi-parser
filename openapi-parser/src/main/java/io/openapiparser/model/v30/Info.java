/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#info-object">4.7.2 Info Object</a>
 */
public class Info extends Properties implements Extensions {

    public Info (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getTitle () {
        return getStringOrThrow (TITLE);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public @Nullable String getTermsOfService () {
        return getStringOrNull (TERMS_OF_SERVICE);
    }

    public @Nullable Contact getContact () {
        return getObjectOrNull (CONTACT, Contact.class);
    }

    public @Nullable License getLicense () {
        return getObjectOrNull (LICENSE, License.class);
    }

    @Required
    public String getVersion () {
        return getStringOrThrow (VERSION);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
