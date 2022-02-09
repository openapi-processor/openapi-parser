/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

import static io.openapiparser.schema.Keywords.HASH;

public class Ref {
    private final String ref;

    public Ref (String ref) {
        this.ref = ref;
    }

    public boolean hasPointer () {
        return ref.contains (HASH);
    }

    private String getDocument () {
        return ref.substring (0, ref.indexOf (HASH));
    }

    public String getPointer () {
        return ref.substring(ref.indexOf (HASH));
    }

    public URI getDocumentUri (URI source) {
        if (ref.startsWith (HASH)) {
            // pointer in document
            return source;

        } else if (ref.indexOf (HASH) > 0) {
            // file with pointer
            return source.resolve (getDocument ());

        } else {
            // file without pointer
            return source.resolve (ref);
        }
    }

    @Override
    public String toString () {
        return ref;
    }
}
