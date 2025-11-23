/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson3;


import io.openapiprocessor.interfaces.Writer;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Jackson 3 JSON writer implementation.
 */
public class JacksonJsonWriter implements Writer {
    private final java.io.Writer writer;
    private final ObjectMapper mapper;

    /**
     * create a Jackson writer.
     *
     * @param writer the target writer.
     */
    public JacksonJsonWriter(java.io.Writer writer) {
        this.writer = writer;

        JsonFactory json = JsonFactory.builder()
                .build();

        mapper = new ObjectMapper(json);
    }

    /**
     * create a Jackson writer.
     *
     * @param writer the target writer.
     * @param json a JSON factory.
     */
    public JacksonJsonWriter(java.io.Writer writer, JsonFactory json) {
        this.writer = writer;
        mapper = new ObjectMapper(json);
    }

    @Override
    public void write(Object document) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(writer, document);
    }
}
