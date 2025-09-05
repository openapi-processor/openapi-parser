/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiprocessor.jsonschema.converter.NoValueException;
import io.openapiparser.support.Required;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * the <em>Reference</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.1.html#reference-object">4.8.23 Reference Object</a>
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
     *
     * // todo do not throw, let getRefObject() throw
     */
    @Required
    String getRef ();

    @Nullable String getSummary();

    @Nullable String getDescription();
}
