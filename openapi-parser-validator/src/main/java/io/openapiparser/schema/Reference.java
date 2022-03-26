/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

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
    private final Ref ref;
    private final RefValue value;

    public Reference (Ref ref, RefValue value) {
        this.ref = ref;
        this.value = value;
    }

    /**
     * the uri of the referenced document.
     *
     * @return the referenced document.
     */
    public URI getDocumentUri () {
        return ref.getDocumentUri ();
    }

    /**
     * the scope at the reference.
     */
    public URI getRefScope () {
        return ref.getScope ();
    }

    /**
     * the scope at the reference value.
     */
    public URI getValueScope () {
        return value.getScope ();
    }

    /**
     * the raw value of the reference. Maybe a simple value, array or map.
     *
     * @return the raw value.
     */
    public Object getRawValue () {
        return value.getValue ();
    }

    @SuppressWarnings ({"unchecked", "TypeParameterUnusedInFormals"})
    public <T> T getValue () {
        return (T) value.getValue ();
    }

    @Override
    public String toString () {
        return ref.toString ();
    }

    private static String createRefRelative (String ref) {
        final int hash = ref.indexOf ("#");
        if (hash != -1) {
            return ref.substring (hash + 1);
        }

        return "";
    }
}
