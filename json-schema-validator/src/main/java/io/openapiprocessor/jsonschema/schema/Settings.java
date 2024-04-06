/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Settings {
    private @Nullable SchemaVersion version;
    private boolean loadSchemas;

    public Settings() {
        version = null;
        loadSchemas = false;
    }

    public Settings version(SchemaVersion version) {
        this.version = version;
        return this;
    }

    public Settings loadSchemas(boolean load) {
        this.loadSchemas = load;
        return this;
    }

    public @Nullable SchemaVersion getVersion() {
        return version;
    }

    public boolean isLoadSchemas() {
        return loadSchemas;
    }
}
