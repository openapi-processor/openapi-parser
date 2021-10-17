/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.support.Strings;

import java.net.URI;

public class ReferenceResolver {
    private static final String RESOLVE_ERROR = "failed to resolve %s.";

    private final Reader reader;
    private final Converter converter;

    private Node baseNode;

    public ReferenceResolver (Reader reader, Converter converter) {
        this.reader = reader;
        this.converter = converter;
    }

    public void resolve(URI baseUri) throws ResolverException {
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

    public Node getBaseNode () {
        return baseNode;
    }
}
