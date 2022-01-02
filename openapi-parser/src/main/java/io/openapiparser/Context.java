/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNotNullConverter;
import io.openapiparser.converter.StringNullableConverter;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.Keywords.REF;

public class Context {
    private final URI baseUri;
    private final ReferenceResolver resolver;

    private Bucket bucket;

    public Context (URI baseUri, ReferenceResolver resolver) {
        this.baseUri = baseUri;
        this.resolver = resolver;
        this.bucket = Bucket.empty ();
    }

    public void read () throws ContextException {
        try {
            resolver.resolve ();
            bucket = resolver.getObject ();
        } catch (Exception e) {
            throw new ContextException (String.format ("failed to read %s.", baseUri), e);
        }
    }

    public Bucket getBucket () {
        return bucket;
    }

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

        return getRefObject (ref, ref);
    }

    public @Nullable Bucket getRefObjectOrThrow (Bucket bucket) {
        String ref = bucket.convert (REF, new StringNotNullConverter ());
        return getRefObject (ref, ref);
    }

    // todo parameters
    public @Nullable Bucket getRefObject(String path, String ref) {
        final Reference reference = getReference (ref);
        final Map<String, Object> value = reference.getValue ();
        if (value == null) {
            return null;
            // todo throw?
//            throw new ContextException (String.format ("$ref'erenced value %s is null", path));
        }

        return new Bucket (JsonPointer.fromFragment (reference.getRef ()), value);
    }
}
