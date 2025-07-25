/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.converter.NoValueException;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Overlay</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/overlay/v1.0.0.html#overlay-object">4.4.1 Overlay Object</a>
 */
public class Overlay extends Properties implements Extensions {

    public Overlay (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Required
    public String getOverlay () {
        return getStringOrThrow (OVERLAY);
    }

    public @Nullable String getExtends () {
        return getStringOrNull (EXTENDS);
    }

    @Required
    public Info getInfo () {
        return getObjectOrThrow (INFO, Info.class);
    }

    @Required
    public Collection<Action> getActions () {
        Collection<Action> actions = getObjectsOrEmpty(ACTIONS, Action.class);
        if (actions == null || actions.isEmpty()) {
            throw new NoValueException(bucket.getLocation().append(ACTIONS));
        }
        return actions;
    }

    @Override
    public Map<String, @Nullable Object> getExtensions() {
        return super.getExtensions ();
    }
}
