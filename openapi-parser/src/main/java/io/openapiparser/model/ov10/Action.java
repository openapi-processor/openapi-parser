/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Action</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/overlay/v1.0.0.html#action-object">4.4.3 Action Object</a>
 */
public class Action extends Properties implements Extensions {

    public Action(Context context, Bucket bucket) {
        super(context, bucket);
    }

    @Required
    public String getTarget() {
        return getStringOrThrow (TARGET);
    }

    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public @Nullable Object getUpdate () {
        return getRawValue (UPDATE);
    }

    public @Nullable <T> T getUpdate (Class<T> api) {
        return getObjectOrThrow(UPDATE, api);
    }

    public @Nullable Boolean getRemove () {
        return getBooleanOrNull (REMOVE);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions() {
        return super.getExtensions ();
    }
}
