package io.openapiparser;

import java.net.URI;

public class Context {
    private static final String READ_ERROR = "failed to read %s.";

    private final URI baseUri;
    private final ReferenceResolver resolver;

//    private ReferenceRegistry registry;
    private Node baseNode;


    public Context (URI baseUri, ReferenceResolver resolver) {
        this.baseUri = baseUri;
        this.resolver = resolver;
//        registry = new ReferenceRegistry (baseUri);
    }

    public void read () throws ContextException {
        try {
            resolver.resolve (baseUri);
            baseNode = resolver.getBaseNode ();
        } catch (Exception e) {
            throw new ContextException (String.format (READ_ERROR, baseUri), e);
        }
    }

    public Node getBaseNode() {
        return baseNode;
    }

    public URI getBaseUri () {
        return baseUri;
    }
}
