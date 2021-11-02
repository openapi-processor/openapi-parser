/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import java.util.LinkedHashMap;

/**
 * the <em>Paths</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#paths-object">4.8.8 Paths Object</a>
 */
public class Paths implements Extensions {
    private LinkedHashMap<String, PathItem> pathItems = new LinkedHashMap<> ();

}
