/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.snakeyaml;

import io.openapiprocessor.interfaces.Writer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class SnakeYamlWriter implements Writer {
    private final java.io.Writer writer;
    private final Yaml yaml;

    public SnakeYamlWriter(java.io.Writer writer) {
        this.writer = writer;

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        yaml = new Yaml(options);
    }

    public SnakeYamlWriter(java.io.Writer writer, Yaml yaml) {
        this.writer = writer;
        this.yaml = yaml;
    }

    @Override
    public void write(Object document) {
        yaml.dump(document, writer);
    }
}
