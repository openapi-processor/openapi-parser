/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson3;

import io.openapiprocessor.interfaces.Writer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.io.IOException;

public class JacksonYamlWriter implements Writer {
    private final java.io.Writer writer;
    private final ObjectMapper mapper;

    public JacksonYamlWriter(java.io.Writer writer) {
        this.writer = writer;

        YAMLFactory yaml = YAMLFactory.builder()
                .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
                .enable(YAMLWriteFeature.MINIMIZE_QUOTES)
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
