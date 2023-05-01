/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.ouput;

import java.util.Collection;

public class OutputUnitDetailed implements OutputUnit {
    @Override
    public boolean isValid () {
        return false;
    }

    @Override
    public String getKeywordLocation () {
        return null;
    }

    @Override
    public String getInstanceLocation () {
        return null;
    }

    @Override
    public String getAbsoluteKeywordLocation () {
        return null;
    }

    @Override
    public String getError () {
        return null;
    }

    @Override
    public Collection<OutputUnit> getErrors () {
        return null;
    }

    @Override
    public Object getAnnotation () {
        return null;
    }

    @Override
    public Collection<OutputUnit> getAnnotations () {
        return null;
    }
}
