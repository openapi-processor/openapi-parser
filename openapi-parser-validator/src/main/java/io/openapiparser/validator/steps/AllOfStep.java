/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

public class AllOfStep extends CompositeStep {

    @Override
    public boolean isValid () {
//        return getSteps ().stream ()
//            .allMatch (ValidationStep::isValid);

        for (ValidationStep step : steps) {
            boolean valid = step.isValid ();
            if (!valid) {
                return false;
            }
        }

        return true;
    }
}
