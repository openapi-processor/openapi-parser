/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

import java.util.Collection;

public interface OutputUnitWrite {

    /**
     * Set the validation result.
     *
     * @param valid the validation result.
     */
    void setValid (boolean valid);

    /**
     * Set the relative location of the validating keyword.
     *
     * @param location the relative location of the validating keyword.
     */
    void setKeywordLocation (String location);

    /**
     * The location of the validated JSON value within the instance.
     *
     * @param location the location of the validated JSON value within the instance.
     */
    void setInstanceLocation (String location);

    /**
     * The absolute, dereferenced location of the validating keyword.
     *
     * @param location the absolute, dereferenced location of the validating keyword.
     */
    void setAbsoluteKeywordLocation (String location);

    /**
     * Set error or annotation produced by a failed validation.
     *
     * @param error the error or annotation produced by a failed validation.
     */
    void setError (String error);

    /**
     * Set the error or annotation produced by a successful validation.
     *
     * @param annotation the error or annotation produced by a successful validation.
     */
    void setAnnotation (Object annotation);

    /**
     * Set errors or annotation produced by a failed validation.
     *
     * @param errors the errors or annotation produced by a failed validation.
     */
    void setErrors (Collection<OutputUnit> errors);

    /**
     * Set the errors or annotation produced by a successful validation.
     *
     * @param annotations the errors or annotations produced by a successful validation.
     */
    void setAnnotations (Collection<OutputUnit> annotations);
}
