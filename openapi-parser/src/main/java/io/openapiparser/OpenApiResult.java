/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.interfaces.Writer;
import io.openapiprocessor.jsonschema.schema.SchemaStore;
import io.openapiprocessor.jsonschema.validator.Validator;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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
     * Experimental. Bundle the document. The bundled document has to be parsed to navigate its OpenAPI model.
     *
     * @return a raw bundled copy of the OpenAPI document.
     */
    Map<String, Object> bundle();

    /**
     * Experimental. Write the document. This will produce useful results only if the document is a single file
     * document. Bundling can be used to create a single file document.
     *
     * @param writer the target writer
     */
    void write(Writer writer) throws IOException;

    /**
     * apply an OpenAPI overlay to the OpenAPI document.
     * @param overlay the overlay document
     */
    void apply(OverlayResult overlay);

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
