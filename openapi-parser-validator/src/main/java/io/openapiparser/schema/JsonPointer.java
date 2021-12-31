/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.NoValueException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON pointer abstraction based on <a href="https://datatracker.ietf.org/doc/html/rfc6901">
 *     rfc6901</a>.
 */
public class JsonPointer {
    public final static JsonPointer EMPTY = new JsonPointer();

    final private @Nullable String pointer;
    final private Collection<String> tokens;

    /**
     * creates a json pointer from a json pointer string. Throws if the string is no valid json
     * pointer.
     *
     * @param jsonPointer json pointer
     * @return a json pointer object
     */
    static public JsonPointer fromJsonPointer (String jsonPointer) {
        if (jsonPointer == null) {
            return EMPTY;
        }

        return new JsonPointer(jsonPointer);
    }

    /**
     * creates a json pointer from a uri fragment string. Throws if the fragment is no valid json
     * pointer.
     *
     * @param fragment json pointer fragment
     * @return a json pointer object
     */
    static public JsonPointer fromFragment (String fragment) {
        if (fragment == null) {
            return EMPTY;
        }

        if (!fragment.startsWith ("#")) {
            throw new JsonPointerInvalidException (fragment);
        }

        return fromJsonPointer (decodeFragment (fragment.substring (1)));
    }

    private JsonPointer () {
        pointer = null;
        tokens = Collections.emptyList ();
    }

    private JsonPointer(String jsonPointer) {
        this.pointer = jsonPointer;

        if (jsonPointer.isEmpty ()) {
            this.tokens = Collections.emptyList ();
            return;
        }

        if (!jsonPointer.startsWith ("/")) {
            throw new JsonPointerInvalidException (jsonPointer);
        }

        tokens = Arrays.stream (jsonPointer.substring (1).split ("/"))
            .map (escaped -> escaped
                .replace ("~1", "/")
                .replace ("~0", "~")
            )
            .collect (Collectors.toList ());
    }

    /**
     * creates a new json pointer appending the given token.
     *
     * @param token token to append
     * @return new json pointer
     */
    public JsonPointer append (String token) {
        if (pointer == null) {
            return new JsonPointer ("/" + token);
        } else {
            return new JsonPointer (pointer + "/" + token);
        }
    }

    /**
     * creates a ewn json pointer appending the giveen token.
     *
     * @param token token to append
     * @return new json pointer string
     */
    public String getJsonPointer (String token) {
        if (pointer == null) {
            return "/" + token;
        } else {
            return pointer + "/" + token;
        }
    }

    /**
     * extracts the value the json pointer point to from the given document object. Throws if there
     * is no value or the pointer does not match the document structure.
     *
     * @param document "json" document
     * @return the value at the pointer location
     */
    public Object getValue(Object document) {
        if (pointer == null)
            return document;

        Object value = document;
        for (String token : tokens) {
            if (value instanceof Map) {
                value = getObjectValue (value, token);
            } else if (value instanceof Collection) {
                value = getArrayValue (value, token);
            }
        }
        return value;
    }

    @Override
    public String toString () {
        return pointer;
    }

    private Object getArrayValue (Object value, String token) {
        value = asArray (value).toArray ()[indexFrom (token)];
        if (value == null) {
            throw new NoValueException(pointer);
        }
        return value;
    }

    private Object getObjectValue (Object value, String token) {
        value = asObject (value).get (token);
        if (value == null) {
            throw new NoValueException (pointer);
        }
        return value;
    }

    private Collection<Object> asArray (Object value) {
        //noinspection unchecked
        return (Collection<Object>) value;
    }

    private Map<String, Object> asObject (Object value) {
        //noinspection unchecked
        return (Map<String, Object>) value;
    }

    private int indexFrom (String token) {
        try {
            return Integer.parseInt (token);
        } catch (NumberFormatException ex) {
            throw new JsonPointerInvalidException (pointer, token, ex);
        }
    }

    private static String decodeFragment (String fragment) {
        try {
            return URLDecoder.decode (fragment, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new JsonPointerInvalidException (fragment, ex);
        }
    }
}
