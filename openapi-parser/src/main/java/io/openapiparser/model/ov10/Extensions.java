/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * the <em>Specification Extensions</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/overlay/v1.0.0.html#specification-extensions">
 *   4.6 Specification Extensions
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
