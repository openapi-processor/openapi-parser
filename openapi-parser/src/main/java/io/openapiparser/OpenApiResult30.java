/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.SchemaStore;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.messages.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class OpenApiResult30 implements OpenApiResult {
    public static final String OPENAPI_SCHEMA = "/openapi/schemas/v3.0/schema.yaml";

    private final Context context;

    private Collection<ValidationMessage> validationMessages;

    public OpenApiResult30 (Context context) {
        this.context = context;
        this.validationMessages = Collections.emptyList ();
    }

    @Override
    public Version getVersion () {
        return Version.V30;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T> T getModel (Class<T> api) {
        if (!OpenApi.class.equals (api)) {
            throw new IllegalArgumentException ();
        }

        return (T)new OpenApi (context, context.getBucket ());
    }

    @Override
    public Collection<ValidationMessage> getValidationMessages () {
        return validationMessages;
    }

    public boolean validate (Validator validator, SchemaStore schemaStore) {
        final JsonSchema schema = schemaStore.addSchema (OPENAPI_SCHEMA);
        validationMessages = validator.validate (schema, context.getRawObject ());
        return validationMessages.isEmpty ();
    }

}

/*
        val converter = JacksonConverter()
        val draftSource = ValidateSpec::class.java.getResourceAsStream("/json/draft-04/schema.json")
        val draft4Object = converter.convert(Strings.of(draftSource))

 */
