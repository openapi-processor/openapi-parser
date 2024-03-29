/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

/**
 * JSON pointer abstraction based on <a href="https://datatracker.ietf.org/doc/html/rfc6901">rfc6901</a>.
 */
public class JsonPointer {
    private static final JsonPointer EMPTY_JSON_POINTER = new JsonPointer();

    final private @Nullable String pointer;
    final private List<String> tokens;

    public static JsonPointer empty () {
        return EMPTY_JSON_POINTER;
    }

    public static JsonPointer from (@Nullable String jsonPointer) {
        if (jsonPointer == null) {
            return EMPTY_JSON_POINTER;
        }

        String pointer = jsonPointer;
        if (jsonPointer.startsWith ("#")) {
            pointer = decode (jsonPointer.substring (1));
        }

        return new JsonPointer(pointer);
    }

    private JsonPointer () {
        pointer = null;
        tokens = Collections.emptyList ();
    }

    private JsonPointer(String jsonPointer) {
        if (jsonPointer.isEmpty ()) {
            this.pointer = null;
            this.tokens = Collections.emptyList ();
            return;
        }

        this.pointer = jsonPointer;
        if (!jsonPointer.startsWith ("/")) {
            throw new JsonPointerInvalidException (jsonPointer);
        }

        tokens = Arrays.stream (jsonPointer.substring (1).split ("/", -1))
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

        return Uris.createUri ("#/" + escaped);
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
    public List<String> getTokens () {
        return unmodifiableList (tokens);
    }

    public boolean isEmpty() {
        return pointer == null;
    }

    @Override
    public boolean equals (@Nullable Object o) {
        if (this == o)
            return true;

        if (o == null || getClass () != o.getClass ())
            return false;

        JsonPointer that = (JsonPointer) o;
        return Objects.equals (pointer, that.pointer);
    }

    @Override
    public int hashCode () {
        return Objects.hash (pointer);
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
