/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.*;

/**
 * validate uniqueItems.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.4">
 *     Draft 4: uniqueItems
 * </a>
 */
public class UniqueItems {
    private final URI uri;

    public UniqueItems (URI uri) {
        this.uri = uri;
    }

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Collection<Object> instanceValue = instance.asCollection ();
        if (schema.isUniqueItems ()) {
            Set<Object> items = new HashSet<> ();
            for (Object item : instanceValue) {
                if (!items.add (item)) {
                    messages.add (new UniqueItemsError (uri.toString ()));
                }
            }
        }

        return messages;
    }
}
