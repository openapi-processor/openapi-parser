/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.snakeyaml;

import io.openapiprocessor.interfaces.Writer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * SnakeYaml writer implementation.
 */
public class SnakeYamlWriter implements Writer {
    private final java.io.Writer writer;
    private final Yaml yaml;

    /**
     * create a SnakeYaml writer.
     *
     * @param writer the target writer.
     */
    public SnakeYamlWriter(java.io.Writer writer) {
        this.writer = writer;

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        yaml = new Yaml(options);
    }

    /**
     * create a SnakeYaml writer.
     *
     * @param writer the target writer.
     * @param yaml a YAML factory.
     */
    public SnakeYamlWriter(java.io.Writer writer, Yaml yaml) {
        this.writer = writer;
        this.yaml = yaml;
    }

    @Override
    public void write(Object document) {
        yaml.dump(document, writer);
    }
}
