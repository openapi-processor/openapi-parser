/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.support.ValidInt;

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
        boolean valid = isValid (instanceValue);
        if (!valid) {
            messages.add (new IpV6Error (instance.getPath ()));
        }

        return messages;
    }

    boolean isValid(String ip) {
        // netmask
        if (ip.contains ("/")) {
            return false;
        }

        List<String> octets = expandZeros (ip);
        if (octets.isEmpty ()) {
            return false;
        }

        int count = 0;
        for (String octet : octets) {
            // only ascii characters
            if (!octet.codePoints().allMatch(c -> c < 128)) {
                return false;
            }

            ValidInt value = ValidInt.parseHex (octet);

            if (value.isValid () && value.isInRange (0, 0xffff)) {
                count++;
            } else {
                return false;
            }
        }

        return count == 8;
    }

    private List<String> expandZeros (String ip) {
        int firstZeroShortcut = ip.indexOf ("::");
        int lastZeroShortcut = ip.lastIndexOf ("::");

        if (firstZeroShortcut != lastZeroShortcut) {
            return Collections.emptyList ();
        }

        List<String> octets = new ArrayList<> ();
        List<String> parts = Arrays.asList (ip.split (":", -1));

        // single colon at start
        int firstColon = ip.indexOf (":");
        if (firstColon == 0 && firstColon != firstZeroShortcut) {
            return Collections.emptyList ();
        }

        // single colon at end
        int lastColon = ip.lastIndexOf (":");
        if (lastColon == ip.length () && lastColon != lastZeroShortcut + 1) {
            return Collections.emptyList ();
        }


        String lastPart = parts.get (parts.size () - 1);
        boolean lastIsIp4 = IpV4.isValid (lastPart);
        if (lastIsIp4) {
            parts = parts.subList (0, parts.size () - 1);
            parts = new ArrayList<> (parts);

            String[] split = lastPart.split ("\\.");

            ValidInt p1 = ValidInt.parse (split[0]);
            ValidInt p2 = ValidInt.parse (split[1]);
            ValidInt p3 = ValidInt.parse (split[2]);
            ValidInt p4 = ValidInt.parse (split[3]);
            int seven = (p1.getValue () << 8) + p2.getValue ();
            int eight = (p3.getValue () << 8) + p4.getValue ();
            parts.add (Integer.toHexString (seven));
            parts.add (Integer.toHexString (eight));
        }

        long cntZeros = 0;
        if (firstZeroShortcut >= 0) {
            cntZeros = 8 - parts.stream ()
                .filter (p -> !p.isEmpty ())
                .count ();
        }

        boolean addedZeros = false;
        for (String part : parts) {
            if (part.isEmpty () && cntZeros > 0 && !addedZeros) {
                for (int z = 0; z < cntZeros; z++) {
                    octets.add ("0");
                }
                addedZeros = true;
            }

            if (part.isEmpty ()) {
                continue;
            }

            octets.add (part);
        }
        return octets;
    }
}
