/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNullableConverter;
import io.openapiparser.schema.*;
import io.openapiparser.schema.ReferenceRegistry;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.schema.Keywords.HASH;
import static io.openapiparser.schema.Keywords.REF;

/**
 * the context is used to resolve $ref's.
 *
 * All model objects have a context. Objects from the same source file will re-use the same context.
 * Objects from different source files will have different {@link Context} objects. If a $ref links
 * to another source file the "current" context will create a new context with the new source file.
 */
public class Context {
    private final URI baseUri;
    private final ReferenceRegistry references;

    public Context (URI baseUri, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.references = references;
    }

    public io.openapiparser.schema.Reference getReference (String ref) {
        if (ref.startsWith (HASH)) {
            // same document
            return references.getRef(baseUri.resolve (ref));
        } else {
            // other document
            if (ref.contains (HASH)) {
                // with path fragment
                final int idxHash = ref.indexOf (HASH);
                String document = ref.substring (0, idxHash);
                String fragment = ref.substring (idxHash);
                URI documentUri = baseUri.resolve (document);
                URI refUri = documentUri.resolve (fragment);
                return references.getRef(refUri);
            } else {
                // full document
                URI refUri = baseUri.resolve (ref);
                return references.getRef(refUri);
            }
        }
    }

    public @Nullable Bucket getRefObjectOrNull (Bucket bucket) {
        String ref = bucket.convert (REF, new StringNullableConverter ());
        if (ref == null)
            return null;

        return getRefObject (ref);
    }

    public Bucket getRefObjectOrThrow (Bucket bucket) {
        final Bucket refObject = getRefObjectOrNull (bucket);
        if (refObject == null)
            throw new RuntimeException ();

        return refObject;
    }

    public JsonInstanceContext getInstanceContext () {
        return new JsonInstanceContext (baseUri, references);
    }

    private @Nullable Bucket getRefObject(String ref) {
        io.openapiparser.schema.Reference reference = getReference (ref);
        Map<String, Object> value = reference.getValue ();
        if (value == null) {
            // todo if it is null it was not resolved, throw?
            return null;
        }
        return new Bucket (reference.getDocumentUri (), reference.getRefRelative (), value);
    }

    /**
     * get a context with the given source uri.
     *
     * If this context has the same source uri it will return itself otherwise it will create a new
     * context with the given source uri.
     *
     * @param source the new source uri.
     * @return context with the source uri
     */
    public Context withSource (URI source) {
        if (baseUri.equals (source)) {
            return this;
        }
        return new Context (source, references);
    }
}
