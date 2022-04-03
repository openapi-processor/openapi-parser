/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import java.util.Collection;
import java.util.stream.Collectors;

public class FlatStep extends CompositeStep {

    public Collection<ValidationStep> getSteps () {
        return steps.stream ()
            .filter (s -> ! (s instanceof NullStep))
            .collect(Collectors.toList());
    }
}
