/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.support.ValidInt;

import java.util.ArrayList;
import java.util.Collection;

/**
 * validates ipv4.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.4">
 *     Draft 4: ipv4
 * </a>
 */
public class IpV4 {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String format = schema.getFormat ();
        if (format == null || !format.equals ("ipv4"))
            return messages;

        String instanceValue = instance.asString ();
        boolean valid = isValid (instanceValue);
        if (!valid) {
            messages.add (new IpV4Error (instance.getPath ()));
        }

        return messages;
    }

    boolean isValid(String ip) {
        String[] addressSignificant = ip.split ("/", 1);
        if (addressSignificant.length == 0) {
            return false;
        }

        if (addressSignificant.length == 2) {
            ValidInt significantBits = ValidInt.parse (addressSignificant[1]);
            if (!significantBits.isValid ()) {
                return false;
            }

            if (!significantBits.isInRange (1, 32)) {
                return false;
            }
        }

        String[] bytes = addressSignificant[0].split ("\\.");
        if (bytes.length != 4) {
            return false;
        }

        for (int i = 0; i < bytes.length; i++) {
            String value = bytes[i];

            // only ascii characters
            if (!value.codePoints().allMatch(c -> c < 128)) {
                return false;
            }

            // no leading zeros
            if (value.length () > 1 && value.startsWith ("0")) {
                return false;
            }

            ValidInt bInt = ValidInt.parse (value);
            if (i == 0 && !bInt.isInRange (1, 255)) {
                return false;
            }

            if (i > 0 && !bInt.isInRange (0, 255)) {
                return false;
            }
        }

        return true;
    }
}
