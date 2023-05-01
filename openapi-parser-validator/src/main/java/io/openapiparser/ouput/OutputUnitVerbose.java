/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.ouput;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class OutputUnitVerbose implements OutputUnit, OutputUnitWrite {

    private boolean valid;
    private String keywordLocation;
    private String instanceLocation;
    private String absoluteKeywordLocation;
    private @Nullable String error;
    private @Nullable Object annotation;
    private @Nullable Collection<OutputUnit> errors;
    private @Nullable Collection<OutputUnit> annotations;

    @Override
    public boolean isValid () {
        return valid;
    }

    @Override
    public String getKeywordLocation () {
        return keywordLocation;
    }

    @Override
    public String getInstanceLocation () {
        return instanceLocation;
    }

    @Override
    public String getAbsoluteKeywordLocation () {
        return absoluteKeywordLocation;
    }

    @Override
    public @Nullable String getError () {
        return error;
    }

    @Override
    public @Nullable Object getAnnotation () {
        return annotation;
    }

    @Override
    public @Nullable Collection<OutputUnit> getErrors () {
        return errors;
    }

    @Override
    public @Nullable Collection<OutputUnit> getAnnotations () {
        return annotations;
    }

    @Override
    public void setValid (boolean valid) {
        this.valid = valid;
    }

    @Override
    public void setKeywordLocation (String location) {
        this.keywordLocation = location;
    }

    @Override
    public void setInstanceLocation (String location) {
        this.instanceLocation = location;
    }

    @Override
    public void setAbsoluteKeywordLocation (String location) {
        this.absoluteKeywordLocation = location;
    }

    @Override
    public void setError (@Nullable String error) {
        this.error = error;
    }

    @Override
    public void setAnnotation (@Nullable Object annotation) {
        this.annotation = annotation;
    }

    @Override
    public void setErrors (@Nullable Collection<OutputUnit> errors) {
        this.errors = errors;
    }

    @Override
    public void setAnnotations (@Nullable Collection<OutputUnit> annotations) {
        this.annotations = annotations;
    }
}
