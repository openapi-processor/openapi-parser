/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator;

import io.openapiprocessor.jsonschema.schema.Format;
import io.openapiprocessor.jsonschema.schema.Output;
import io.openapiprocessor.jsonschema.schema.SchemaVersion;

import java.util.Arrays;
import java.util.EnumSet;

public class ValidatorSettings {
    private SchemaVersion version = SchemaVersion.getLatest ();
    private Output output = Output.VERBOSE;

    private final EnumSet<Format> formats = EnumSet.noneOf(Format.class);

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

    /**
     * set the output format.
     *
     * @param output the expected output format
     * @return this
     */
    public ValidatorSettings setOutput (Output output) {
        this.output = output;
        return this;
    }

    /**
     * get output format.
     *
     * @return the output format, defaults to {@code Output.VERBOSE}.
     */
    public Output getOutput () {
        return output;
    }

    /**
     * disable formats, i.e. that should not be validated.
     *
     * @param disable formats to disable
     */
    public void disableFormats (Format... disable) {
        Arrays.asList (disable).forEach (formats::remove);
    }

    /**
     * enable formats, i.e. formats that should be validated.
     *
     * @param enable format
     */
    public void enableFormats (Format... enable) {
        formats.addAll (Arrays.asList (enable));
    }

    /**
     * check if a format should be validated
     *
     * @param format the format
     * @return true if it should be validated, else false
     */
    public boolean validateFormat (Format format) {
        return formats.contains (format);
    }
}
