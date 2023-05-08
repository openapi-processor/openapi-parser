/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.reader;

import io.openapiprocessor.interfaces.Reader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * simple reader implementation that creates an input stream of a fixed string.
 *
 * Probably only useful for testing.
 */
public class StringReader implements Reader {
    private final String content;

    public StringReader (String content) {
        this.content = content;
    }

    @Override
    public InputStream read (URI uri) {
        return new ByteArrayInputStream (content.getBytes(StandardCharsets.UTF_8));
    }
}
