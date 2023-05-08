/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

import java.util.Collection;

public interface OutputUnitWrite {

    /**
     * set validation result
     */
    void setValid (boolean valid);

    /**
     * relative location of the validating keyword
     */
    void setKeywordLocation (String location);

    /**
     * The location of the validated JSON value within the instance
     */
    void setInstanceLocation (String location);

    /**
     * The absolute, dereferenced location of the validating keyword
     */
    void setAbsoluteKeywordLocation (String location);

    void setError (String error);

    void setAnnotation (Object annotation);

    /**
     * errors or annotation produced by a failed validation
     */
    void setErrors (Collection<OutputUnit> errors);

    /**
     * errors or annotation produced by a successful validation
     */
    void setAnnotations (Collection<OutputUnit> annotations);
}

