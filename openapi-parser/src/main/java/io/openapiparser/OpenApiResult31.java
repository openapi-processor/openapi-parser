/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v31.validations.OpenapiValidator;
import io.openapiparser.model.v31.OpenApi;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class OpenApiResult31 implements OpenApiResult {
    private final Context context;
    private Collection<ValidationMessage> validationMessages;

    public OpenApiResult31 (Context context) {
        this.context = context;
        validationMessages = Collections.emptyList ();
    }

    @Override
    public Version getVersion () {
        return Version.V31;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T> T getModel (Class<T> api) {
        if (!OpenApi.class.equals (api)) {
            throw new IllegalArgumentException ();
        }

        return (T) new OpenApi (context, context.getBaseNode ());
    }

    @Override
    public Collection<ValidationMessage> getValidationMessages () {
        return validationMessages;
    }

    void validate() {
        validationMessages = new OpenapiValidator ().validate (
            new ValidationContext (context.getBaseUri ()), context.getBaseNode ());
    }

}
