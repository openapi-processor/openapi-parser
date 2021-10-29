/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class Reference {
    private final URI parentUri;
    private final URI refUri;
    private final String ref;
    private final Object rawValue;

    public Reference (URI parentUri, URI refUri, String ref, Object rawValue) {
        this.parentUri = parentUri;
        this.refUri = refUri;
        this.ref = ref;
        this.rawValue = rawValue;
    }

    public Object getRawValue () {
        return rawValue;
    }
}
