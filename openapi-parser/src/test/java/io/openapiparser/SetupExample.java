/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.Reader;
import io.openapiprocessor.jsonschema.reader.UriReader;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.result.MessageTextBuilder;
import io.openapiprocessor.snakeyaml.SnakeYamlConverter;

import java.util.Collection;

public class SetupExample {

    void parseAndValidate () {
        Reader reader = new UriReader ();
        Converter converter = new SnakeYamlConverter ();
        // Converter converter = new JacksonConverter ();
        DocumentLoader loader = new DocumentLoader (reader, converter);

        DocumentStore documents = new DocumentStore ();
        Resolver.Settings resolverSettings = new Resolver.Settings (SchemaVersion.Draft4);
        Resolver resolver = new Resolver (documents, loader, resolverSettings);

        // parser OpenAPI file or url
        OpenApiParser parser = new OpenApiParser (resolver);
        OpenApiResult result = parser.parse ("openapi.yaml");
        // OpenAPI 3.1.x with model.v31.OpenAPI import
        OpenApi model = result.getModel (OpenApi.class);

        // validate OpenAPI
        SchemaStore store = new SchemaStore (loader);
        ValidatorSettings settings = new ValidatorSettings ();
        Validator validator = new Validator (settings);
        boolean valid = result.validate (validator, store);

        // print validation errors
        Collection<ValidationError> errors = result.getValidationErrors ();
        ValidationErrorTextBuilder builder = new ValidationErrorTextBuilder ();

        for (ValidationError error : errors) {
            System.out.println (builder.getText(error));
        }
    }
}
