package io.openapiparser;

import io.openapiparser.support.Strings;

import java.net.URI;
import java.util.Map;

public class ReferenceResolver {
    private static final String RESOLVE_ERROR = "failed to resolve %s.";

    private final URI baseUri;
    private final Reader reader;
    private final Converter converter;

    private Map<String, Object> baseNode;

    public ReferenceResolver (URI baseUri, Reader reader, Converter converter) {
        this.baseUri = baseUri;
        this.reader = reader;
        this.converter = converter;
    }

    public void resolve() throws ResolverException {
        try {
            baseNode = converter.convert (Strings.of (reader.read (baseUri)));

            // todo
            // find references
            // resolve references
            // register references

        } catch (Exception e) {
            throw new ResolverException (String.format (RESOLVE_ERROR, baseUri), e);
        }
    }

    public Map<String, Object> getBaseNode () {
        return baseNode;
    }
}
