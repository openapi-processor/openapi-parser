/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.*;

/**
 * validates minProperties.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.2">
 *     Draft 4: minProperties
 * </a>
 */
public class MinProperties {
    private final URI uri;

    public MinProperties (URI uri) {
        this.uri = uri;
    }

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());
        Integer minProperties = schema.getMinProperties ();

        if (minProperties == null || instanceProperties.size () >= minProperties)
            return messages;

        messages.add (new MinPropertiesError (uri.toString (), minProperties));
        return messages;
    }
}
