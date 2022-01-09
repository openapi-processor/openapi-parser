/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

/**
 * resolved $ref.
 *
 * <ul>
 *   <li>$ref string must be an uri.</li>
 *   <li>
 *     fragment of $ref is a <a href="https://datatracker.ietf.org/doc/html/rfc6901">json pointer</a>
 *     ('~' encoded as '~0', '/' encoded as '~1').
 *   </li>
 * </ul>
 */
public class Reference {
    private final URI parentUri;
    private final URI documentUri;
    private final String ref;
    private final String refRelative;
    private final Object rawValue;

    public Reference (URI parentUri, URI documentUri, String ref) {
        this.parentUri = parentUri;
        this.documentUri = documentUri;
        this.ref = ref;
        this.refRelative = createRefRelative (ref);
        this.rawValue = "unresolved reference";
    }

    public Reference (URI parentUri, URI documentUri, String ref, Object rawValue) {
        this.parentUri = parentUri;
        this.documentUri = documentUri;
        this.ref = ref;
        this.refRelative = createRefRelative (ref);
        this.rawValue = rawValue;
    }

    public Reference withRawValue (Object rawValue) {
        return new Reference (parentUri, documentUri, ref, rawValue);
    }

    /**
     * the uri of the referenced document.
     *
     * @return the referenced document.
     */
    public URI getDocumentUri () {
        return documentUri;
    }

    /**
     * the original $ref value. This may contain a file name, e.g. "foo.yaml#/Foo".
     *
     * @return the original ref.
     */
    public String getRef () {
        return ref;
    }

    /**
     * the original $ref value but without file name, e.g. "/Foo".
     *
     * @return the relative ref.
     */
    public String getRefRelative () {
        return refRelative;
    }

    /**
     * the raw value of the reference. Maybe a simple value, array or map.
     *
     * @return the raw value.
     */
    public Object getRawValue () {
        return rawValue;
    }

    @SuppressWarnings ({"unchecked", "TypeParameterUnusedInFormals"})
    public <T> T getValue () {
        return (T) rawValue;
    }

    private String createRefRelative (String ref) {
        final int hash = ref.indexOf ("#");
        if (hash != -1) {
            return ref.substring (hash + 1);
        }

        // full file ref
        if (this.ref.equals (ref)) {
            return "";
        }

        return ref;
    }
}
