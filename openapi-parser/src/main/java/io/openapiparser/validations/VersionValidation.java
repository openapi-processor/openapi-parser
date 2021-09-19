package io.openapiparser.validations;

import io.openapiparser.Validation;
import io.openapiparser.ValidationContext;
import io.openapiparser.ValidationMessage;

import java.util.*;
import java.util.regex.Pattern;

/**
 * validate openapi version
 */
public class VersionValidation implements Validation {
    private static final String OPENAPI = "openapi";
    private static final Pattern VERSION = Pattern.compile ("\\d\\.\\d\\.\\d");

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Map<String, Object> node) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Object version = node.get (OPENAPI);
        if (!(version instanceof String) || !isValid((String)version)) {
            messages.add(createMessage (context, version));
        }

        return messages;
    }

    private ValidationMessage createMessage (ValidationContext context, Object version) {
        return new ValidationMessage(context.getPropertyPath (OPENAPI), createText (version));
    }

    private boolean isValid (String version) {
        return VERSION.matcher (version).matches ();
    }

    private String createText(Object value) {
        return String.format ("'%s' is not a valid version number", value);
    }

}

