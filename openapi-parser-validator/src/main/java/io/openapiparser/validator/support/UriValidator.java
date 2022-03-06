/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support;

/**
 * Validates URI based on <a href="https://datatracker.ietf.org/doc/html/rfc3986">rfc3986</a>.
 *
 * just a start...
 */
public class UriValidator {
    private static class ValidatorException extends RuntimeException { }

    private final String source;

    public UriValidator (String source) {
        this.source = source;
    }

    public boolean validate () {
        try {
            int componentStart = 0;
            int componentEnd = findScheme ();
            String scheme = getComponent (componentStart, componentEnd);
            checkScheme (scheme);

            componentStart = componentEnd + 1;
            componentEnd = findAuthority (componentStart);
            String authority = getComponent (componentStart, componentEnd);
            checkAuthority (authority);

            return true;

        } catch (ValidatorException ex) {
            return false;
        }
    }

    private int findAuthority (int start) {
        int pos = start;
        int length = source.length ();

        if (pos >= length) {
            return pos;
        }

        String authorityStart = source.substring (pos, pos + 2);
        if (!authorityStart.equals ("//")) {
            return pos;
        }

        pos += 2;
        String authorityStopOn = "/?#";

        for (int i = pos; i < length; i++) {
            char c = source.charAt (i);

            if (authorityStopOn.indexOf (c) == 0) {
                break;
            }

            pos++;
        }
        return pos;
    }

    private void checkAuthority (String authority) {
        // may be empty
        if (authority == null || authority.length () == 0)
            return;

        String content = authority.substring (2);
        int hostStart = 0;
        int hostEnd = content.length ();

        int hostSeparator = content.indexOf (AT);
        if (hostSeparator != -1) {
            String userInfo = content.substring (0, hostSeparator);
            checkUserInfo (userInfo);
            hostStart = hostSeparator + 1;
        }

        int bracketClose = content.indexOf (CLOSE_BRACKET);
        int portSeparator = content.lastIndexOf (COLON);
        boolean hasPort = portSeparator > hostSeparator && portSeparator > bracketClose;
        if (hasPort) {
            String port = content.substring (portSeparator);
            // todo check port
            hostEnd = portSeparator;
        }

        String host = content.substring (hostStart, hostEnd);
        if (host.isEmpty ()) {
            return;
        }

        if (isIpV6 (host)) {
            return;
        }

        if (isIpV4 (host)) {
            return;
        }

        // reg-name
        for (int i = 0; i < host.length (); i++) {
            int c = host.codePointAt (i);

            if (c == PERCENT && host.length () > i + 2) {
                String encoded = host.substring (i + 1, i + 2);
                ValidInt validInt = ValidInt.parseHex (encoded);
                if (!validInt.isValid ()) {
                    throw new ValidatorException ();
                }
            } else {
                boolean valid = isUnreserved (c)
                    || isSubDelim (c);

                if (!valid) {
                    throw new ValidatorException ();
                }
            }
        }
    }

    private boolean isIpV6 (String source) {
        if (source.length () <= 2) {
            return false;
        }

        if (!isEnclosedIp (source)) {
            return false;
        }

        String ip = source.substring (1, source.length () - 1);
        boolean isIpV6 = new IpV6Validator (ip).validate ();
        if (isIpV6) {
            return true;
        }

        throw new ValidatorException ();
    }

    private boolean isIpV4 (String source) {
        return new IpV4Validator (source).validate ();
    }

    private boolean isEnclosedIp (String source) {
        return source.indexOf (OPEN_BRACKET) == 0
            && source.lastIndexOf (CLOSE_BRACKET) == source.length () - 1;
    }

    private void checkUserInfo (String userInfo) {
        for (int i = 0; i < userInfo.length (); i++) {
            int c = userInfo.codePointAt (i);

            if (c == PERCENT && userInfo.length () > i + 2) {
                String encoded = userInfo.substring (i + 1, i + 2);
                ValidInt validInt = ValidInt.parseHex (encoded);
                if (!validInt.isValid ()) {
                    throw new ValidatorException ();
                }
            } else {
                boolean valid = isUnreserved (c)
                    || isSubDelim (c)
                    || c == COLON;

                if (!valid) {
                    throw new ValidatorException ();
                }
            }
        }
    }

    private int findScheme () {
        int pos = 0;
        int length = source.length ();

        String noSchemeOn = "/?#";
        String schemeStopOn = ":";

        for (int i = pos; i < length; i++) {
            char c = source.charAt (i);
            if (noSchemeOn.indexOf (c) >= 0) {
                break;
            }

            if (schemeStopOn.indexOf (c) == 0) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private void checkScheme(String scheme) {
        if (scheme == null || scheme.length () == 0) {
            throw new ValidatorException ();
        }

        int first = scheme.codePointAt (0);
        if (!isLetter(first)) {
            throw new ValidatorException ();
        }

        if (scheme.length () == 1) {
            return;
        }

        boolean allMatch = scheme.substring (1)
            .codePoints ()
            .allMatch (this::isSchemeChar);

        if (!allMatch) {
            throw new ValidatorException ();
        }
    }

    private String getComponent (int start, int end) {
        return source.substring (start, end);
    }

    private boolean isUnreserved (int c) {
        return isLetter (c)
            || isNumber (c)
            || c == HYPHEN
            || c == DOT
            || c == UNDERSCORE
            || c == TILDE;
    }

    private boolean isSubDelim (int c) {
        return c == EXCLAMATION_MARK
            || c == DOLLAR
            || c == AMPERSAND
            || c == SINGLE_QUOTE
            || c == OPEN_PARENTHESIS
            || c == CLOSE_PARENTHESIS
            || c == ASTERISK
            || c == PLUS
            || c == COMMA
            || c == SEMICOLON
            || c == EQUALS;
    }

    private boolean isSchemeChar (int c) {
        return isLetter (c)
            || isNumber (c)
            || c == PLUS
            || c == HYPHEN
            || c == DOT;
    }

    private boolean isNumber (int c) {
        return c >= 0x30 && c <= 0x39;
    }

    private boolean isLetter (int c) {
        return (c >= 0x41 && c <= 0x5a) || (c >= 0x61 && c <= 0x7a);
    }

    private static final int EXCLAMATION_MARK = 0x21;
    private static final int DOLLAR = 0x24;
    private static final int PERCENT = 0x25;
    private static final int AMPERSAND = 0x26;
    private static final int SINGLE_QUOTE = 0x27;
    private static final int OPEN_PARENTHESIS = 0x28;
    private static final int CLOSE_PARENTHESIS = 0x29;
    private static final int ASTERISK = 0x2a;
    private static final int PLUS = 0x2b;
    private static final int COMMA = 0x2c;
    private static final int HYPHEN = 0x2d;
    private static final int DOT = 0x2e;
    private static final int COLON = 0x3a;
    private static final int SEMICOLON = 0x3b;
    private static final int EQUALS = 0x3d;
    private static final int AT = 0x40;
    private static final int OPEN_BRACKET = 0x5b;
    private static final int CLOSE_BRACKET = 0x5d;
    private static final int UNDERSCORE = 0x5f;
    private static final int TILDE = 0x7e;
}
