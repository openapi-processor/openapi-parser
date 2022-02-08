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

    public JsonPointer getPointer () {
        return JsonPointer.from (ref.substring(ref.indexOf (HASH)));
    }

    public String getAbsoluteRef (URI source) {
        int hash = ref.indexOf("#");
        if (hash == -1) {
            return source.toString ();
        }

        String pointer = ref.substring(hash);
        pointer = JsonPointerSupport.encodePath (pointer);
        return source.resolve (pointer).toString ();
    }

    public URI getDocumentUri (URI source) {
        if (ref.startsWith (HASH)) {
            // pointer in same document
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

    private String getDocument () {
        return ref.substring (0, ref.indexOf (HASH));
    }
}
