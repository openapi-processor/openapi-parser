/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

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
        throw new RuntimeException ();
    }

    @Override
    public String getInstanceLocation () {
        throw new RuntimeException ();
    }

    @Override
    public String getAbsoluteKeywordLocation () {
        throw new RuntimeException ();
    }

    @Override
    public String getError () {
        throw new RuntimeException ();
    }

    @Override
    public Object getAnnotation () {
        throw new RuntimeException ();
    }

    @Override
    public Collection<OutputUnit> getErrors () {
        throw new RuntimeException ();
    }

    @Override
    public Collection<OutputUnit> getAnnotations () {
        throw new RuntimeException ();
    }
}
