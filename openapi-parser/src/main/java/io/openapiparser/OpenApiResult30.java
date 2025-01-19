/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiprocessor.interfaces.Writer;
import io.openapiprocessor.jsonschema.ouput.OutputConverter;
import io.openapiprocessor.jsonschema.ouput.OutputUnit;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_30;
import static io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_30_ID;

public class OpenApiResult30 implements OpenApiResult {
    private final Context context;
    private final Bucket root;

    private final DocumentStore documents;

    private Collection<ValidationError> validationErrors = List.of();

    public OpenApiResult30 (Context context, Bucket root, DocumentStore documents) {
        this.context = context;
        this.root = root;
        this.documents = documents;
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
    public Map<String, Object> bundle () {
        return new OpenApiBundler (context, documents, root).bundle ();
    }

    @Override
    public void write(Writer writer) throws IOException {
        writer.write(root.getRawValues());
    }

    @Override
    public Map<String, Object> apply(OverlayResult overlayResult) {
        OverlayApplier overlay = new OverlayApplier(root.getRawValues());
        return overlay.apply(overlayResult);
    }

    @Override
    public Collection<ValidationError> getValidationErrors () {
        return validationErrors;
    }

    public boolean validate (Validator validator, SchemaStore schemaStore) {
        try {
            schemaStore.register (OPENAPI_SCHEMA_30_ID, OPENAPI_SCHEMA_30);
            JsonSchema schema = schemaStore.getSchema (OPENAPI_SCHEMA_30_ID, SchemaVersion.Draft4);

            Object bundle = bundle ();
            JsonInstance instance = new JsonInstance (bundle);
            ValidationStep result = validator.validate (schema, instance);

            OutputConverter converter = new OutputConverter (Output.BASIC);
            OutputUnit output = converter.convert (result);

            if (output.isValid ()) {
                validationErrors = Collections.emptyList ();
                return true;
            }

            Collection<OutputUnit> errors = output.getErrors ();
            assert errors != null;

            validationErrors = errors
                .stream ()
                .map (e -> {
                    return new ValidationError (
                        e.getInstanceLocation (),
                        e.getKeywordLocation (),
                        e.getAbsoluteKeywordLocation (),
                        e.getError ()
                    );
                })
                .collect (Collectors.toList ());

            return false;
        } catch (Exception ex) {
            return true;
        }
    }
}
