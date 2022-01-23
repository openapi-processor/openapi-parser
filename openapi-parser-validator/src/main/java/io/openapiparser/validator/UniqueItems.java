/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.messages.*;

import java.net.URI;
import java.util.*;

/**
 * validate uniqueItems.
 *
 * <p>See specification:
 * <ul>
 *     <li>
 *      <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.4">Draft 4: uniqueItems</a>
 *     </li>
 * </ul>
 *
 * draft4 - 5.12 ????
 */
class UniqueItems {
    private final URI uri;

    UniqueItems (URI uri) {
        this.uri = uri;
    }

    Collection<ValidationMessage> validate (JsonSchema schema, Collection<Object> source) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        if (schema.isUniqueItems ()) {
            Set<Object> items = new HashSet<> ();
            for (Object item : source) {
                if (!items.add (item)) {
                    messages.add (new UniqueItemsError (uri.toString ()));
                }
            }
        }

        return messages;
    }
}
