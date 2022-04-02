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

    public int visitCnt = 0;

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
     * get full ref uri with scope and json pointer.
     *
     * @return the full ref uri
     */
    public URI getFullRefUri () {
        return ref.getFullRefUri ();
    }

    /**
     * the value of the reference.
     */
    public String getRefValue () {
        return ref.getRef ();
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

    public JsonPointer getPointer() {
        return JsonPointer.from (ref.getPointer ());
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
