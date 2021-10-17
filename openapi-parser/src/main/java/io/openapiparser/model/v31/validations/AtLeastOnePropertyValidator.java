/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31.validations;

import io.openapiparser.*;

import java.util.*;
import java.util.stream.Collectors;

public class AtLeastOnePropertyValidator implements Validator {
    private final Collection<String> properties;

    public AtLeastOnePropertyValidator (Collection<String> properties) {
        this.properties = properties;
    }

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Set<String> found = properties.stream ()
            .filter (node::containsKey)
            .collect(Collectors.toSet());

        if(found.isEmpty ()) {
            messages.add (createMessage (context, properties));
        }

        return messages;
    }

    private ValidationMessage createMessage (ValidationContext context, Collection<String> properties) {

        return new ValidationMessage(context.getPropertyPath (properties), createText (properties));
    }

    private String createText(Collection<String> properties) {
        return String.format ("one of '%s' is missing, at least one is required",
            String.join ("|", this.properties));
    }
}
