/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Specification Extensions</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#specification-extensions">
 *   4.8 Specification Extensions
 * </a>
 */
public interface Extensions {

    /**
     * map of all extension properties.
     *
     * @return map of extension properties
     */
    Map<String, @Nullable Object> getExtensions ();

}
