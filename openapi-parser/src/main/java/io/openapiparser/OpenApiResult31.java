/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v31.OpenApi;
import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.Validator;

import java.util.Collection;
import java.util.Collections;

public class OpenApiResult31 implements OpenApiResult {
    @Deprecated
    public static final String OPENAPI_SCHEMA = "/openapi/schemas/v3.1/schema.yaml";

    private final Context context;
    private final Bucket root;

    private Collection<ValidationMessage> validationMessages;

    public OpenApiResult31 (Context context, Bucket root) {
        this.context = context;
        this.root = root;
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

        return (T) new OpenApi (context, root);
    }

    @Override
    public boolean validate (Validator validator, SchemaStore schemaStore) {
        // not yet supported...

//        JsonSchema schema = schemaStore.addSchema (OPENAPI_SCHEMA_31_ID, OPENAPI_SCHEMA_31);
//        JsonInstance instance = new JsonInstance (root.getRawValues (), context.getInstanceContext ());
//        ValidationStep result = validator.validate (schema, instance);
//        validationMessages = result.getMessages ();
//        return validationMessages.isEmpty ();
        return true;
    }

    @Override
    public Collection<ValidationMessage> getValidationMessages () {
        return validationMessages;
    }
}
