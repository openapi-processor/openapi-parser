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
    private final Object rawValue;

    public Reference (URI parentUri, URI documentUri, String ref) {
        this.parentUri = parentUri;
        this.documentUri = documentUri;
        this.ref = ref;
        this.rawValue = "unresolved reference";
    }

    public Reference (URI parentUri, URI documentUri, String ref, Object rawValue) {
        this.parentUri = parentUri;
        this.documentUri = documentUri;
        this.ref = ref;
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
     * the original $ref value.
     *
     * @return the original ref.
     */
    public String getRef () {
        return ref;
    }

    /**
     * the raw value of the reference. Maybe a simple value, array or map.
     *
     * @return the raw value.
     */
    public Object getDocumentValue () {
        return rawValue;
    }

    @SuppressWarnings ({"unchecked", "TypeParameterUnusedInFormals"})
    public <T> T getValue () {
        return (T) rawValue;
    }
}
