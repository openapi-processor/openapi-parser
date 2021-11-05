/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Required;

/**
 * the <em>Example</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#example-object">4.7.19 Example Object</a>
 */
public class Example implements Reference {

    @Override
    public boolean isRef () {
        return false;
    }

    @Override
    @Required
    public String getRef () {
        return null;
    }
}
