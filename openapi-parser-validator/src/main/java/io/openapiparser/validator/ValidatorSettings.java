/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.SchemaVersion;

public class ValidatorSettings {
    private SchemaVersion version = SchemaVersion.Draft6;

    public boolean isDraft4 () {
        return SchemaVersion.Draft4.equals (version);
    }

    /**
     * get the JSON schema version that should be used if a schema doesn't provide a $schema. The
     * default is {@code Version.Latest}.
     *
     * @return the JSON schema version.
     */
    public SchemaVersion getVersion () {
        return version;
    }

    /**
     * set a JSON schema version. It is a fallback version that is used if a schema doesn't provide
     * a $schema.
     *
     * @param version JSON schema version
     */
    public void setVersion (SchemaVersion version) {
        this.version = version;
    }
}
