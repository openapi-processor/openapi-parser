/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support;

/**
 * rfc5321, rfc5322
 */
public class EmailValidator {
    String email;

    public EmailValidator (String email) {
        this.email = email;
    }

    public boolean validate () {
        int endLocalPart = findLocalPart ();
        if (endLocalPart < 0)
            return false;

        String localPart = email.substring (0, endLocalPart);
        String domain = email.substring (endLocalPart + 1);

        if (domain.isEmpty ())
            return false;

        boolean validLocalPart = validateLocalPart (localPart);
        boolean validDomain = validateDomain (domain);

        return validLocalPart && validDomain;
    }

    private boolean validateLocalPart (String localPart) {
        int length = localPart.length ();

        char cStart = localPart.charAt (0);
        char cEnd = localPart.charAt (length - 1);
        boolean quoted = cStart == DOUBLE_QUOTE && cEnd == DOUBLE_QUOTE;

        // at least two double quotes
        if (quoted && length < 2)
            return false;

        String source = localPart;
        if (quoted) {
            source = localPart.substring (1, length - 2);
        }

        // empty string is ok ...
        if (source.isEmpty ())
            return true;

        if (quoted) {
            return isQuotedString (source);
        } else {
            return isDotString (source);
        }
    }

    private boolean validateDomain (String domain) {
        int length = domain.length ();

        char cStart = domain.charAt (0);
        char cEnd = domain.charAt (length - 1);
        boolean address = cStart == OPEN_BRACKET && cEnd == CLOSE_BRACKET;

        // at least two brackets
        if (address && length < 2)
            return false;

        String source = domain;
        if (address) {
            source = domain.substring (1, length - 1);

            boolean ipv4 = new IpV4Validator(source).validate ();
            if (ipv4)
                return true;

            boolean ipv6Tag = source.startsWith ("IPv6:");
            if (!ipv6Tag)
                return false;

            String ipv6Source = source.substring ("IPv6:".length ());
            boolean ipv6 = new IpV6Validator(ipv6Source).validate ();
            return ipv6;
        } else {
            return isDomain (source);
        }
    }

    private boolean isDomain (String source) {
        String[] bytes = source.split ("\\.");

        for (String part : bytes) {
            int c = part.codePointAt (0);
            int l = part.codePointAt (part.length () - 1);

            // first must be letter or digit
            if (!isLetter (c) && !isNumber (c))
                return false;

            // last must be letter or digit
            if (!isLetter (l) && !isNumber (l))
                return false;

            for (int i = 1; i < part.length () - 1; i++) {
                int mc = part.codePointAt (i);

                // in between must be letter, digit or dash
                if (!isLetter (mc) && !isNumber (mc) && mc != '-')
                    return false;
            }
        }
        return true;
    }


    private int findLocalPart () {
        boolean quoted = email.startsWith ("\"");
        String stopOn = quoted ? "\"@" : "@";

        int stop = email.lastIndexOf (stopOn);
        if (stop < 0)
            return stop;

        return stop + (quoted ? 1 : 0);
    }

    private boolean isDotString (String source) {
        if (source.startsWith (".") || source.endsWith ("."))
            return false;

        if (source.contains (".."))
            return false;

        String[] bytes = source.split ("\\.");

        for (String part : bytes) {
            if (!part.codePoints ().allMatch (c -> isAtom ((char) c))) {
                return false;
            }
        }
        return true;
    }

    private boolean isQuotedString (String source) {
        for (int i = 0; i < source.length(); i++) {
            if (!isQContent (source, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isQContent (String source, int pos) {
        return isQText (source, pos) || isQPair (source, pos);
    }

    private boolean isAtom (char c) {
        return isAText (c);
    }

    private boolean isAText (char c) {
        return isLetter (c)
            || isNumber (c)
            || c == '!'
            || c == '#'
            || c == '$'
            || c == '%'
            || c == '&'
            || c == '\''
            || c == '*'
            || c == '+'
            || c == '-'
            || c == '/'
            || c == '='
            || c == '?'
            || c == '^'
            || c == '_'
            || c == '`'
            || c == '{'
            || c == '|'
            || c == '}'
            || c == '~';
    }

    private boolean isQText (String source, int pos) {
        char c = source.charAt (pos);
        return (c >= 32 && c <= 33) || (c >= 35 && c <= 91) || (c >= 93 && c <= 126);
    }

    private boolean isQPair (String source, int pos) {
        char c = source.charAt (pos);
        char e = source.charAt (pos);
        return c == BACK_SLASH && (e >= 32 && e <= 126);
    }

    private boolean isNumber (int c) {
        return c >= 0x30 && c <= 0x39;
    }

    private boolean isLetter (int c) {
        return (c >= 0x41 && c <= 0x5a) || (c >= 0x61 && c <= 0x7a);
    }


    private static final int DOUBLE_QUOTE = 34;
    private static final int AT = 64;
    private static final int OPEN_BRACKET = 91;
    private static final int BACK_SLASH = 92;
    private static final int CLOSE_BRACKET = 93;
}
