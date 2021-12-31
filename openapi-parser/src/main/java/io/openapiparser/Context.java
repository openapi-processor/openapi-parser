/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.PropertiesBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.Keywords.REF;

public class Context {
    private final URI baseUri;
    private final ReferenceResolver resolver;

    private PropertiesBucket properties;

    public Context (URI baseUri, ReferenceResolver resolver) {
        this.baseUri = baseUri;
        this.resolver = resolver;
        this.properties = PropertiesBucket.empty ();
    }

    public void read () throws ContextException {
        try {
            resolver.resolve ();
            properties = resolver.getProperties ();
        } catch (Exception e) {
            throw new ContextException (String.format ("failed to read %s.", baseUri), e);
        }
    }

    // todo getDocument()
    public PropertiesBucket getProperties () {
        return properties;
    }

    public Reference getReference (String ref) {
        return resolver.resolve (baseUri, ref);
    }

    @Deprecated
    public @Nullable Node getRefNodeOrNull(Node current) {
        String ref = current.getStringValue (REF);
        if (ref == null)
            return null;

        return getRefNode (current.getPath (), ref);
    }

    @Deprecated
    public @Nullable Node getRefNodeOrNull(@Nullable String ref) {
        if (ref == null)
            return null;

        return getRefNode (ref);
    }

    @Deprecated
    public @Nullable Node getRefNode(String ref) {
        final Reference reference = getReference (ref);
        final Map<String, Object> value = reference.getValue();
        if (value == null) {
            // todo throw ResolverException ?
            return null;
        }
        return new Node (String.format ("%s.$ref(%s)", "....", reference.getRef ()), value);
    }

    @Deprecated
    public @Nullable Node getRefNode(String path, String ref) {
        final Reference reference = getReference (ref);
        final Map<String, Object> value = reference.getValue();
        if (value == null) {
            // todo throw ResolverException ?
            return null;
        }
        return new Node (String.format ("%s.$ref(%s)", path, reference.getRef ()), value);
    }

    @Deprecated
    public Node getBaseNode() {
        return new Node(baseUri.toString (), properties.getRawValues ());
    }

    @Deprecated
    public URI getBaseUri () {
        return baseUri;
    }
}
