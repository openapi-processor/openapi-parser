/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates maxItems.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.2">
 *     Draft 4: maxItems
 * </a>
 */
public class MaxItems {
    private final URI uri;

    public MaxItems (URI uri) {
        this.uri = uri;
    }

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Collection<Object> instanceValue = instance.asCollection ();
        Integer maxItems = schema.getMaxItems ();
        if (maxItems == null || instanceValue.size () <= maxItems)
            return messages;

        messages.add (new MaxItemsError (uri.toString (), maxItems));
        return messages;
    }
}
