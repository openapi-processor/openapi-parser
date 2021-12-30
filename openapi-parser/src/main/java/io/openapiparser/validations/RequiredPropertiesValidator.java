/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validations;

import io.openapiparser.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * validates existence of required properties on the given node.
 */
@Deprecated
public class RequiredPropertiesValidator implements Validator {
    private final Collection<String> properties;

    public RequiredPropertiesValidator (Collection<String> properties) {
        this.properties = properties;
    }

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return properties.stream ()
            .filter (prop -> !node.hasProperty (prop))
            .map (prop -> createMessage (context, prop))
            .collect(Collectors.toList());
    }

    private ValidationMessage createMessage (ValidationContext context, String property) {
        return new ValidationMessage(context.getPropertyPath (property), createText (property));
    }

    private String createText(String property) {
        return String.format ("'%s' is a missing but required property", property);
    }
}
