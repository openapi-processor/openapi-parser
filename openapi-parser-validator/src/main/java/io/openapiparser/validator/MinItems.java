/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.messages.MinItemsError;
import io.openapiparser.validator.messages.ValidationMessage;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * validates minItems.
 *
 * <p>See specification:
 * <ul>
 *     <li>
 *      <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.3">Draft 4: minItems</a>
 *     </li>
 * </ul>
 *
 * draft4 - 5.11 ???
 */
class MinItems {
    private final URI uri;

    MinItems (URI uri) {
        this.uri = uri;
    }

    Collection<ValidationMessage> validate (
        JsonSchema schema, Collection<Object> source) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        int minItems = schema.getMinItems ();
        if (source.size () < minItems) {
            messages.add (new MinItemsError (uri.toString (), minItems));
        }

        return messages;
    }
}
