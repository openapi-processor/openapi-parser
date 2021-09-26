package io.openapiparser.validations;

import io.openapiparser.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * validates existence of required properties on the given node.
 */
public class RequiredValidation implements Validation {
    private final Collection<String> properties;

    public RequiredValidation (Collection<String> properties) {
        this.properties = properties;
    }

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return properties.stream ()
            .filter (prop -> !node.containsKey (prop))
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
