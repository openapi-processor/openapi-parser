package io.openapiparser.schema;

import java.net.URI;

public class JsonSchemaContext {
    private final URI baseUri;
    private final ReferenceRegistry references;

    public JsonSchemaContext (URI baseUri, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.references = references;
    }

    public Reference getReference (URI ref) {
        URI resolve = baseUri.resolve (ref);
        return references.getRef (resolve);
    }

    public JsonSchemaContext withSource (URI source) {
        if (baseUri.equals (source)) {
            return this;
        }
        return new JsonSchemaContext (source, references);
    }
}
