/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.SchemaStore;
import io.openapiprocessor.jsonschema.validator.Validator;

import java.util.Collection;

public interface OverlayResult {

    enum Version { V10 }

    /**
     * get the OpenAPI {@link OverlayResult.Version} of the OpenAPI description.
     *
     * @return the {@link OverlayResult.Version}
     */
    Version getVersion ();

    /**
     * get the OpenAPI Overlay model. {@code T} must be
     * <ul>
     *   <li>{@link io.openapiparser.model.ov10.Overlay}  if the version is {@code V10}</li>
     * </ul>
     * otherwise, it will throw an {@link IllegalArgumentException}
     *
     * @param api class of the OpenAPI Overlay model
     * @param <T> OpenAPI Overlay model type
     * @return OpenAPI Overlay model
     */
    <T> T getModel (Class<T> api);

    /**
     * run schema validation. Retrieve the collection of validation messages from
     * {@link #getValidationErrors()}.
     *
     * @param validator json schema validator
     * @param schemaStore json schema store
     * @return true if valid, else false
     */
    boolean validate (Validator validator, SchemaStore schemaStore);

    /**
     * validation errors.
     *
     * @return the validation errors.
     */
    Collection<ValidationError> getValidationErrors ();
}
