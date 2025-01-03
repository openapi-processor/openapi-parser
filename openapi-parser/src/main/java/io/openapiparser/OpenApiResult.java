/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.SchemaStore;
import io.openapiprocessor.jsonschema.validator.Validator;

import java.util.Collection;

/**
 * OpenAPI parser result.
 */
public interface OpenApiResult {

    enum Version { V30, V31 }

    /**
     * get the OpenAPI {@link Version} of the OpenAPI description.
     *
     * @return the {@link Version}
     */
    Version getVersion ();

    /**
     * get the OpenAPI model. {@code T} must be
     * <ul>
     *   <li>{@link io.openapiparser.model.v30.OpenApi}  if the version is {@code V30}</li>
     *   <li>{@link io.openapiparser.model.v31.OpenApi}  if the version is {@code V31}</li>
     * </ul>
     * otherwise, it will throw an {@link IllegalArgumentException}
     *
     * @param api class of the OpenAPI model
     * @param <T> OpenAPI model type
     * @return OpenAPI model
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
