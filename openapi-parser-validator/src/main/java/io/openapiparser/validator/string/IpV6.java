/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.support.IpV6Validator;

import java.util.*;

/**
 * validates ipv6.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.5">
 *     Draft 4: ipv6
 * </a>
 */
public class IpV6 {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("ipv6"))
            return messages;

        String instanceValue = instance.asString ();
        boolean valid = new IpV6Validator (instanceValue).validate ();
        if (!valid) {
            messages.add (new IpV6Error (instance.getPath ()));
        }

        return messages;
    }
}
