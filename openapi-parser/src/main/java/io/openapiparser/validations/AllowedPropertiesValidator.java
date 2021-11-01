/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validations;

import io.openapiparser.*;

import java.util.Collection;
import java.util.stream.Collectors;

public class AllowedPropertiesValidator implements Validator {
    public enum Extensions {INCLUDE_X, EXCLUDE_X}

    private final Collection<String> properties;
    private final boolean extensions;

    public AllowedPropertiesValidator (Collection<String> properties) {
        this.properties = properties;
        this.extensions = false;
    }

    public AllowedPropertiesValidator (Collection<String> properties, Extensions extensions) {
        this.properties = properties;
        this.extensions = Extensions.INCLUDE_X.equals (extensions);
    }

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return node.getPropertyNames ().stream ()
            .filter (key -> !isAllowedProperty (key))
            .map (prop -> createMessage (context, prop))
            .collect(Collectors.toList());
    }

    private boolean isAllowedProperty (String key) {
        return properties.contains (key) || isExtensionProperty(key);
    }

    private boolean isExtensionProperty (String key) {
        return extensions && key.startsWith ("x-");
    }

    private ValidationMessage createMessage (ValidationContext context, String property) {
        return new ValidationMessage(context.getPropertyPath (property), createText (property));
    }

    private String createText(String property) {
        return String.format ("'%s' is not an allowed property", property);
    }
}
