/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * validates format: uri.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.6">
 *     Draft 4: uri
 * </a>
 */
public class Uri {
    // https://mathiasbynens.be/demo/url-regex - stephenhay
    private static final String URI_REGEX = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    // accepted protocols without further validation
    private static final List<String> ACCEPTED_PROTOCOLS = Arrays.asList (
        "ldap:", "mailto:", "news:", "tel:", "urn:"
    );

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("uri"))
            return messages;

        String instanceValue = instance.asString ();
        for (String protocol : ACCEPTED_PROTOCOLS) {
            if (instanceValue.startsWith(protocol)) {
                return messages;
            }
        }

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(URI_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();
        if (!valid) {
            messages.add (new UriError (instance.getPath ()));
        }

        return messages;
    }
}
