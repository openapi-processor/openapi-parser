/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.openapiprocessor.interfaces.Writer;

import java.io.IOException;

public class JacksonYamlWriter implements Writer {
    private final java.io.Writer writer;
    private final ObjectMapper mapper;

    public JacksonYamlWriter(java.io.Writer writer) {
        this.writer = writer;

        YAMLFactory yaml = YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .build();

        mapper = new ObjectMapper(yaml);
    }

    public JacksonYamlWriter(java.io.Writer writer, YAMLFactory yaml) {
        this.writer = writer;
        mapper = new ObjectMapper(yaml);
    }

    @Override
    public void write(Object document) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(writer, document);
    }
}
