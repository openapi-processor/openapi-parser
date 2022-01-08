/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNotNullConverter;
import io.openapiparser.converter.StringNullableConverter;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.Keywords.REF;

/**
 * the context is used to resolve $ref's.
 *
 * All model objects have a context. Objects from the same source file will re-use the same context.
 * Objects from different source files will have different {@link Context} objects. If a $ref links
 * to another source file the "current" context will create a new context with the new source file.
 */
public class Context {
    private final URI baseUri;
    private final ReferenceResolver resolver;

    @Deprecated
    private Bucket bucket; // do we need this????

    public Context (URI baseUri, ReferenceResolver resolver) {
        this.baseUri = baseUri;
        this.resolver = resolver;
        this.bucket = Bucket.empty ();
    }

    @Deprecated
    public void read () throws ContextException {
        try {
            resolver.resolve ();
            bucket = resolver.getObject ();
        } catch (Exception e) {
            throw new ContextException (String.format ("failed to read %s.", baseUri), e);
        }
    }

    // todo rename
    public Bucket readIt () throws ContextException {
        try {
            resolver.resolve ();
            return resolver.getObject ();
        } catch (Exception e) {
            throw new ContextException (String.format ("failed to read %s.", baseUri), e);
        }
    }

    @Deprecated // trying to remove
    public Bucket getBucket () {
        return bucket;
    }

    @Deprecated
    public Map<String, Object> getRawObject () {
        return bucket.getRawValues ();
    }

    public Reference getReference (String ref) {
        return resolver.resolve (baseUri, ref);
    }

    public @Nullable Bucket getRefObjectOrNull (Bucket bucket) {
        String ref = bucket.convert (REF, new StringNullableConverter ());
        if (ref == null)
            return null;

        return getRefObject (ref);
    }

    public @Nullable Bucket getRefObjectOrThrow (Bucket bucket) {
        String ref = bucket.convert (REF, new StringNotNullConverter ());
        return getRefObject (ref);
    }

    public @Nullable Bucket getRefObject(String ref) {
        final Reference reference = getReference (ref);
        final Map<String, Object> value = reference.getValue ();
        if (value == null) {
            return null;
            // todo throw?
//            throw new ContextException (String.format ("$ref'erenced value %s is null", path));
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
     */ // todo should setup internal bucket...  do we need it???
    public Context withSource (URI source) {
        if (baseUri.equals (source)) {
            return this;
        }
        return new Context (source, resolver);
    }
}
