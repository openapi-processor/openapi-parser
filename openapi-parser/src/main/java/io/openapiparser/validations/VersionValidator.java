/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validations;

import io.openapiparser.*;

import java.util.*;
import java.util.regex.Pattern;

import static io.openapiparser.Keywords.OPENAPI;

/**
 * validate {@code "openapi"} version number property.
 *
 * <p>See specification:
 * <ul>
 * <li>4.1 Versions
 *   <ul>
 *     <li><a href="https://spec.openapis.org/oas/v3.1.0.html#versions">3.1.0</a></li>
 *     <li><a href="https://spec.openapis.org/oas/v3.0.3.html#versions">3.0.3</a></li>
 *   </ul>
 * </li>
 * <li>4.7.1 OpenAPI Object
 *   <ul>
 *     <li><a href="https://spec.openapis.org/oas/v3.1.0.html#openapi-object">3.1.0</a></li>
 *     <li><a href="https://spec.openapis.org/oas/v3.0.3.html#openapi-object">3.0.3</a></li>
 *   </ul>
 * </li>
 * </ul>
 */
@Deprecated
public class VersionValidator implements Validator {
    private static final Pattern VERSION = Pattern.compile ("\\d\\.\\d\\.\\d");

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        Object value = node.getRawValue (OPENAPI);
        if (value == null) {
            messages.add (createMessage (context, "null"));

        } else if (!(value instanceof String) || !isValid(value)) {
            messages.add(createMessage (context, value));
        }

        return messages;
    }

    private ValidationMessage createMessage (ValidationContext context, @Nullable Object version) {
        return new ValidationMessage(context.getPropertyPath (OPENAPI), createText (version));
    }

    private boolean isValid (Object version) {
        return VERSION.matcher ((String) version).matches ();
    }

    private String createText(@Nullable Object value) {
        return String.format ("'%s' is not a valid version number", value);
    }

}

