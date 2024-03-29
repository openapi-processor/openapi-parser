/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.support.Required;
import io.openapiprocessor.jsonschema.converter.NoValueException;

/**
 * the <em>Reference</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#reference-object">4.7.23 Reference Object</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-pbryan-zyp-json-ref-03">JSON Reference</a>
 */
public interface Reference {

    /**
     * check if this is a $ref object.
     *
     * @return true if $ref else false.
     */
    boolean isRef ();

    /**
     * $ref value. Should be guarded by {@link #isRef()}. Throws if {@link #isRef()} is false.
     *
     * @return ref
     * @throws NoValueException if {@link #isRef()} is false
     */
    @Required
    String getRef ();
}
