/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.ov10.Overlay;
import io.openapiprocessor.jsonschema.ouput.OutputConverter;
import io.openapiprocessor.jsonschema.ouput.OutputUnit;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.openapiparser.OpenApiSchemas.*;

public class OverlayResult10 implements OverlayResult {
    private final Context context;
    private final Bucket root;

    private Collection<ValidationError> validationErrors = List.of();

    public OverlayResult10 (Context context, Bucket root) {
        this.context = context;
        this.root = root;
    }

    @Override
    public Version getVersion() {
        return Version.V10;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T> T getModel(Class<T> api) {
        if (!Overlay.class.equals (api)) {
            throw new IllegalArgumentException ();
        }

        return (T)new Overlay(context, root);
    }

    @Override
    public boolean validate(Validator validator, SchemaStore schemaStore) {
        try {
            schemaStore.register (OVERLAY_SCHEMA_10_ID, OVERLAY_SCHEMA_10);
            JsonSchema schema = schemaStore.getSchema (OVERLAY_SCHEMA_10_ID, SchemaVersion.Draft202012);

            JsonInstance instance = new JsonInstance (root.getRawValues());
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

    @Override
    public Collection<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
