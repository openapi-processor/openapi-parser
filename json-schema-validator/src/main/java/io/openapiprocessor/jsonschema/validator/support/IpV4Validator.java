/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.support;

/**
 * Validate ip v4 address in "dotted-quad" notation.
 * Based on <a href="https://datatracker.ietf.org/doc/html/rfc2673">rfc2673</a>
 */
public class IpV4Validator {
    private final String ip;

    public IpV4Validator (String ipv4) {
        this.ip = ipv4;
    }

    public boolean validate () {
        // netmask
        if (ip.contains ("/")) {
            return false;
        }

        String[] bytes = ip.split ("\\.");
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
