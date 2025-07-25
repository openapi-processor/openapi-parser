/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.steps.CompositeStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;
import static io.openapiprocessor.jsonschema.support.Types.asString;
import static io.openapiprocessor.jsonschema.support.Types.isString;

public class Id {

    public void validate (JsonSchema schema, JsonInstance instance, CompositeStep parentStep) {
        Map<String, @Nullable Object> properties = getInstanceValue (instance);
        Object rawId = properties.get (Keywords.ID);

        if (rawId == null || !shouldValidateId (schema))
            return;

        IdStep step = new IdStep (schema, instance);

        if (!isString (rawId)) {
            step.setInvalid ();
            parentStep.add (step);
            return;
        }

        // id must not contain fragment, except an empty fragment
        String id = asString (rawId);
        if (id.contains (Keywords.HASH) && !id.endsWith (Keywords.HASH)) {
            step.setInvalid ();
            parentStep.add (step);
            return;
        }

        parentStep.add (step);
    }

    private Map<String, @Nullable Object> getInstanceValue (JsonInstance instance) {
        return requiresNonNull(instance.asObject ());
    }

    private boolean shouldValidateId (JsonSchema schema) {
        return schema.getContext ().getVersion ().isLaterOrEqualTo201909 ();
    }
}
