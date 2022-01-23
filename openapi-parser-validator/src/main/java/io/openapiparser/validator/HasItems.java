/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.messages.ItemsSizeError;
import io.openapiparser.validator.messages.ValidationMessage;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * draft4 - 5.9 - no hasItems in draft 4 ?????
 */
class HasItems {
    private final URI uri;

    HasItems (URI uri) {
        this.uri = uri;
    }

    Collection<ValidationMessage> validate (JsonSchema schema, Collection<Object> source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        JsonSchema.Items has = schema.hasItems ();
        if (has == JsonSchema.Items.MULTIPLE) {
            JsonSchema additional = schema.getAdditionalItems ();
            if (additional != null && additional.isFalse()) {

                final Collection<JsonSchema> items = schema.getItemsCollection ();
                if (source.size () > items.size ()) {
                    messages.add (new ItemsSizeError (uri.toString (), items.size()));
                }
            }
        }

        return messages;
    }
}
