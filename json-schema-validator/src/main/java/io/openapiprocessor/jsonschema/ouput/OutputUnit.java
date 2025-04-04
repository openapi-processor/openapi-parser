/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public interface OutputUnit {

    /**
     * The validation result.
     *
     * @return the validation result.
     */
    boolean isValid ();

    /**
     * The relative location of the validating keyword.
     *
     * @return the relative location of the validating keyword.
     */
    String getKeywordLocation ();

    /**
     * The location of the validated JSON value within the instance.
     *
     * @return the location of the validated JSON value within the instance.
     */
    String getInstanceLocation ();

    /**
     * The absolute, dereferenced location of the validating keyword.
     *
     * @return the absolute, dereferenced location of the validating keyword.
     */
    String getAbsoluteKeywordLocation ();

    @Nullable String getError ();

    @Nullable Object getAnnotation ();

    /**
     * The errors or annotation produced by a failed validation.
     *
     * @return the errors or annotation produced by a failed validation.
     */
    @Nullable Collection<OutputUnit> getErrors ();

    /**
     * The errors or annotation produced by a successful validation.
     *
     * @return the errors or annotation produced by a successful validation.
     */
    @Nullable Collection<OutputUnit> getAnnotations ();
}
