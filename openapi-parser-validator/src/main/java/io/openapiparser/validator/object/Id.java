/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.steps.CompositeStep;

import java.util.Map;

import static io.openapiparser.converter.Types.asString;
import static io.openapiparser.converter.Types.isString;
import static io.openapiparser.support.Nullness.nonNull;

public class Id {

    public void validate (JsonSchema schema, JsonInstance instance, CompositeStep parentStep) {
        Map<String, Object> properties = getInstanceValue (instance);
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

    private Map<String, Object> getInstanceValue (JsonInstance instance) {
        return nonNull(instance.asObject ());
    }

    private boolean shouldValidateId (JsonSchema schema) {
        return schema.getContext ().getVersion ().isLaterOrEqualTo201909 ();
    }
}
