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
     * @param uri uri to check
     *
     * @return true if fragment is a single hash, otherwise false
     */
    public static boolean hasEmptyFragment (URI uri) {
        return uri.toString ().endsWith (EMPTY_FRAGMENT);
    }

    public static URI resolve (URI uri, String child) {
        return resolve(uri, createUri(child));
    }

    public static URI resolve (URI uri, URI child) {
        if (uri.isOpaque()) {
            return resolveOpaque (uri, child);
        }

        return uri.resolve(child);
    }

    private static URI resolveOpaque (URI uri, URI part) {
        if (uri.equals(part)) {
            return uri;
        }

        if (part.isAbsolute()) {
            return part;
        }

        String uriPath = uri.getFragment () != null ? uri.getFragment () : "";
        int uriIdx = uriPath.indexOf ("/");
        String uriId = uriIdx >= 0 ? uriPath.substring (0, uriIdx) : uriPath;
        String uriPointer = uriIdx >= 0 ? uriPath.substring (uriIdx) : "";

        String partPath = part.getFragment () != null ? part.getFragment (): "";
        int partIdx = partPath.indexOf ("/");
        String partId = partIdx >= 0 ? partPath.substring (0, partIdx) : partPath;
        String partPointer = partIdx >= 0 ? partPath.substring (partIdx) : "";

        String id = uriId;
        if (!partId.isEmpty ()) {
            id = partId;
        }

        String uriValue = uri.toString ();
        if (!uriPath.isEmpty ()) {
            uriValue = uriValue.substring (0, uriValue.lastIndexOf ("#"));
        }

        String pointer = resolve (createUri (uriPointer), createUri (partPointer)).toString ();
        String newUri = uriValue;
        if (!id.isEmpty () || !pointer.isEmpty ()) {
            newUri += "#" + id + pointer;
        }

        return createUri(newUri);
    }

    public static URI stripFragment(URI uri) {
        if (uri.isOpaque()) {
            return stripFragmentFromOpaqueUri(uri);
        }

        return stripFragmentFromNonOpaqueUri(uri);
    }

    private static URI stripFragmentFromNonOpaqueUri(URI uri) {
        try {
            return new URI(
                    uri.getScheme(),
                    uri.getUserInfo(),
                    uri.getHost(),
                    uri.getPort(),
                    uri.getPath(),
                    uri.getQuery(),
                    nullable(null)
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException (e.getMessage(), e);
        }
    }

    private static URI stripFragmentFromOpaqueUri(URI uri) {
        String opaque = uri.toString();
        int fragIndex = opaque.lastIndexOf("#");
        if (fragIndex < 0) {
            return uri;
        }
        return createUri(opaque.substring(0, fragIndex));
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

    private static boolean hasFragment (URI uri) {
        return uri.toString().contains("#");
    }

    public static String encode (String source) {
        try {
            return URLEncoder.encode (source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException (ex);
        }
    }

    public static String decode (String source) {
        try {
            return URLDecoder.decode (source, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException (ex);
        }
    }
}
