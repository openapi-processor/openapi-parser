/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class OutputUnitFlag implements OutputUnit {
    private final boolean valid;

    public OutputUnitFlag (boolean valid) {
        this.valid = valid;
    }

    public boolean isValid () {
        return valid;
    }

    @Override
    public String getKeywordLocation () {
        return "";
    }

    @Override
    public String getInstanceLocation () {
        return "";
    }

    @Override
    public String getAbsoluteKeywordLocation () {
        return "";
    }

    @Override
    public @Nullable String getError () {
        return null;
    }

    @Override
    public @Nullable Object getAnnotation () {
        return null;
    }

    @Override
    public @Nullable Collection<OutputUnit> getErrors () {
        return null;
    }

    @Override
    public @Nullable Collection<OutputUnit> getAnnotations () {
        return null;
    }
}
