/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableCollection;

/**
 * JSON pointer abstraction based on <a href="https://datatracker.ietf.org/doc/html/rfc6901">
 *     rfc6901</a>.
 */
public class JsonPointer {
    public final static JsonPointer EMPTY = new JsonPointer();

    final private @Nullable String pointer;
    final private List<String> tokens;

    /**
     * creates a json pointer from a json pointer string. Throws if the string is not a valid json
     * pointer.
     *
     * @param jsonPointer json pointer
     * @return a json pointer object
     */ // of() , from ()??
    @Deprecated
    static public JsonPointer fromJsonPointer (@Nullable String jsonPointer) {
        return from (jsonPointer);
    }

    /**
     * creates a json pointer from a uri fragment string. Throws if the fragment is not a valid json
     * pointer.
     *
     * @param fragment json pointer fragment
     * @return a json pointer object
     */
    @Deprecated
    static public JsonPointer fromFragment (String fragment) {
        return from (fragment);
    }

    static public JsonPointer from (@Nullable String jsonPointer) {
        if (jsonPointer == null) {
            return EMPTY;
        }

        String pointer = jsonPointer;
        if (jsonPointer.startsWith ("#")) {
            pointer = decode (jsonPointer.substring (1));
        }

//        if (!pointer.startsWith ("/")) {
//            return EMPTY;
//        }

        return new JsonPointer(pointer);
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
//            tokens = Collections.emptyList ();
//            return;
            throw new JsonPointerInvalidException (jsonPointer);
        }

        tokens = Arrays.stream (jsonPointer.substring (1).split ("/"))
            .map (JsonPointerSupport::decode)
            .collect (Collectors.toList ());
    }

    /**
     * creates a new json pointer appending the given token.
     *
     * @param token token to append
     * @return new json pointer
     */
    public JsonPointer append (String token) {
        return new JsonPointer (getJsonPointer (token));
    }

    /**
     * creates a new json pointer appending the given index to the pointer.
     *
     * @param index the index
     * @return new json pointer to index
     */
    public JsonPointer append (int index) {
        return append (String.valueOf (index));
    }

    /**
     * creates a new json pointer appending the given token.
     *
     * @param token token to append
     * @return new json pointer string
     */
    public String getJsonPointer (String token) {
        String encoded = JsonPointerSupport.encode (token);

        if (pointer == null) {
            return "/" + encoded;
        } else {
            return pointer + "/" + encoded;
        }
    }

    public URI toUri() {
        if (tokens.isEmpty ())
            return URI.create ("");

        String escaped = tokens.stream ()
            .map (JsonPointerSupport::encode)
            .collect (Collectors.joining ("/"));

        return URI.create ("#/" + escaped);
    }

    /**
     * get the last token of the pointer.
     *
     * @return last token
     */
    public String tail () {
        if (tokens.isEmpty ())
            return "";

        return tokens.get (tokens.size () - 1);
    }

    /**
     * get the last token of the pointer as array index.
     *
     * @return last token array index
     */
    public int tailIndex () {
        try {
            return Integer.parseInt (tokens.get (tokens.size () - 1));
        } catch (NumberFormatException ex) {
            throw new JsonPointerInvalidException (toString (), tail(), ex);
        }
    }

    /**
     * gets the unescaped tokens of this pointer.
     *
     * @return tokens.
     */
    public Iterable<String> getTokens () {
        return unmodifiableCollection (tokens);
    }

    @Override
    public String toString () {
        return pointer != null ? pointer : "";
    }

    private static String encode (String pointer) {
        try {
            return URLEncoder.encode (pointer, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new JsonPointerInvalidException (pointer, ex);
        }
    }

    private static String decode (String pointer) {
        try {
            String encoded = JsonPointerSupport.encodePath (pointer);
            return URLDecoder.decode (encoded, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new JsonPointerInvalidException (pointer, ex);
        }
    }
}
