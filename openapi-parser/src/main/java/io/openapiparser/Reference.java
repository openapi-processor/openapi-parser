/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class Reference {
    private final URI parentUri;
    private final URI docUri;
    private final String ref;
    private final Object rawValue;

    public Reference (URI parentUri, URI docUri, String ref) {
        this.parentUri = parentUri;
        this.docUri = docUri;
        this.ref = ref;
        this.rawValue = null;
    }

    public Reference (URI parentUri, URI docUri, String ref, Object rawValue) {
        this.parentUri = parentUri;
        this.docUri = docUri;
        this.ref = ref;
        this.rawValue = rawValue;
    }

    public Reference withRawValue (Object rawValue) {
        return new Reference (parentUri, docUri, ref, rawValue);
    }

    /**
     * the uri of ref'erenced document.
     *
     * @return the referenced document.
     */
    public URI getDocUri () {
        return docUri;
    }

    /**
     * the original $ref value.
     *
     * @return the original ref.
     */
    public String getRef () {
        return ref;
    }

    public Object getRawValue () {
        return rawValue;
    }
}
