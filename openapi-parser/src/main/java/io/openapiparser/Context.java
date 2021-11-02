/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.Map;

public class Context {

    private final URI baseUri;
    private final ReferenceResolver resolver;

    private Node baseNode;

    public Context (URI baseUri, ReferenceResolver resolver) {
        this.baseUri = baseUri;
        this.resolver = resolver;
        this.baseNode = Node.empty ();
    }

    public Reference getReference(String ref) {
        return resolver.resolve (baseUri, ref);
    }

    public @Nullable Node getRefNode(String ref) {
        final Reference reference = getReference (ref);
        final Map<String, Object> value = reference.getValue();
        if (value == null) {
            // todo throw ResolverException ?
            return null;
        }
        return new Node (String.format ("%s.$ref(%s)", "....", reference.getRef ()), value);
    }

    public void read () throws ContextException {
        try {
            resolver.resolve ();
            baseNode = resolver.getBaseNode ();
        } catch (Exception e) {
            throw new ContextException (String.format ("failed to read %s.", baseUri), e);
        }
    }

    public Node getBaseNode() {
        return baseNode;
    }

    public URI getBaseUri () {
        return baseUri;
    }
}
