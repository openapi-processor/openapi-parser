/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static io.openapiparser.support.Nullness.nullable;

/**
 * Uri support functions.
 *
 * todo move to support package
 */
public class UriSupport {
    // should be private, must not be modified
    @Deprecated // use emptyUri() method
    public static final URI EMPTY_URI = URI.create ("");
    public static final String EMPTY_FRAGMENT = "#";

    public static URI emptyUri () {
        return URI.create ("");
    }

    public static URI emptyFragment () {
        return URI.create ("#");
    }

    public static URI createUri (String source) {
        return URI.create (encodePath (source));
    }

    public static boolean isEmpty(@Nullable URI uri) {
        return uri == null || uri.equals (emptyUri ());
    }

    /**
     * check if the {@code uri} has an empty fragment, i.e a single "#".
     *
     * @return true if fragment is a single hash, otherwise false
     */
    public static boolean hasEmptyFragment (URI uri) {
        return uri.toString ().endsWith (EMPTY_FRAGMENT);
    }

    public static URI stripFragment(URI uri) {
        try {
            return new URI (
                uri.getScheme (),
                uri.getUserInfo (),
                uri.getHost (),
                uri.getPort (),
                uri.getPath (),
                uri.getQuery (),
                nullable (null)
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException (e.getMessage(), e);
        }
    }

    public static URI stripEmptyFragment(URI uri) {
        if (hasEmptyFragment (uri)) {
            return stripFragment (uri);
        }
        return uri;
    }

    public static String encodePath (String source) {
        return source
            .replace ("{", "%7B")
            .replace ("}", "%7D");
    }

    public static String decodePath (String source) {
        return source
            .replace ("%7B", "{")
            .replace ("%7D", "}");
    }

    private static String encode (String source) {
        try {
            return URLEncoder.encode (source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException (ex);
        }
    }

    private static String decode (String source) {
        try {
            return URLDecoder.decode (source, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException (ex);
        }
    }
}
