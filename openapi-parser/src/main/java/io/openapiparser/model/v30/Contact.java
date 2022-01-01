/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.converter.ExtensionsConverter;
import io.openapiparser.converter.StringConverter;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Contact</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#contact-object">4.7.3 Contact Object</a>
 */
public class Contact implements Extensions {
    private final Context context;
    private final PropertyBucket properties;

    public Contact (Context context, PropertyBucket properties) {
        this.context = context;
        this.properties = properties;
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
        return properties.convert (new ExtensionsConverter ());
    }

    private @Nullable String getStringOrNull (String property) {
        return properties.convert (property, new StringConverter ());
    }
}
