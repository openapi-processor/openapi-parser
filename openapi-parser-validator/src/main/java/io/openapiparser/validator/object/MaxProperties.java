/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.*;

/**
 * validates maxProperties.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.1">
 *     Draft 4: maxProperties
 * </a>
 */
public class MaxProperties {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Integer maxProperties = schema.getMaxProperties ();

        if (maxProperties == null || instanceProperties.size () <= maxProperties)
            return messages;

        messages.add (new MaxPropertiesError (instance.getPath (), maxProperties));
        return messages;
    }
}
