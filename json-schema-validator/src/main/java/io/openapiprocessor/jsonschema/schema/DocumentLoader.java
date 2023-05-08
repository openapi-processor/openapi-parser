/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiparser.Converter;
import io.openapiparser.Reader;
import io.openapiprocessor.jsonschema.support.Strings;

import java.io.InputStream;
import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

public class DocumentLoader {
    private final Reader reader;
    private final Converter converter;

    public DocumentLoader (Reader reader, Converter converter) {
        this.reader = reader;
        this.converter = converter;
    }

    public Object loadDocument (URI documentUri) {
        try {
            return converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            throw new DocumentLoaderException (String.format ("failed to download '%s'.", documentUri), e);
        }
    }

    public Object loadDocument (String resourcePath) {
        try {
            InputStream source = nonNull (getClass ().getResourceAsStream (resourcePath));
            return converter.convert (Strings.of (source));
        } catch (Exception e) {
            throw new DocumentLoaderException (String.format ("failed to load '%s'.", resourcePath), e);
        }
    }
}
