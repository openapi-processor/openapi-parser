/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;

import static io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_30;
import static io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_30_ID;

public class OpenApiResult30 implements OpenApiResult {
    private final Context context;
    private final Bucket root;

    private DocumentStore documents;

    private Collection<ValidationMessage> validationMessages;

    @Deprecated
    public OpenApiResult30 (Context context, Bucket root) {
        this.context = context;
        this.root = root;
        this.documents = new DocumentStore ();
        this.validationMessages = Collections.emptyList ();
    }

    public OpenApiResult30 (Context context, Bucket root, DocumentStore documents) {
        this.context = context;
        this.root = root;
        this.documents = documents;
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

        return (T)new OpenApi (context, root);
    }

    @Override
    public Collection<ValidationMessage> getValidationMessages () {
        return validationMessages;
    }

    public boolean validate (Validator validator, SchemaStore schemaStore) {
        schemaStore.register (OPENAPI_SCHEMA_30_ID, OPENAPI_SCHEMA_30);
        JsonSchema schema = schemaStore.getSchema (OPENAPI_SCHEMA_30_ID, SchemaVersion.Draft4);

        Object bundle = bundle ();
        JsonInstanceContext instanceContext = new JsonInstanceContext (root.getScope (), new ReferenceRegistry ());

        JsonInstance instance = new JsonInstance (bundle, instanceContext);
//        JsonInstance instance = new JsonInstance (root.getRawValues (), context.getInstanceContext ());
        ValidationStep result = validator.validate (schema, instance);
        return result.isValid ();

//        OutputConverter converter = new OutputConverter (Output.FLAG);
//        OutputUnit output = converter.convert (result);
//        return output.isValid ();

        // todo extract messages
//        validationMessages = result.getMessages ();
//        return validationMessages.isEmpty ();
    }

    Object bundle () {
        OpenApiBundler bundler = new OpenApiBundler (context, documents, root);
        Object bundle = bundler.bundle ();
        return bundle;
    }
}
