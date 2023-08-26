/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiparser.model.v30.PathItem;
import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.Reader;
import io.openapiprocessor.jsonschema.reader.UriReader;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.snakeyaml.SnakeYamlConverter;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class SetupExampleTest {

    //@Test
    void parseAndValidate () {
        // 1. create a document loader.
        // It loads a document by uri and converts it to a Map<String, Object>
        // object tree that represents the OpenAPI document. The parser
        // operates on that Object tree which makes it independent of the
        // object mapper (e.g. jackson, snakeyaml etc.).
        // Both (Reader and Converter) have a very simple interface which makes
        // it easy to implement your own.
        Reader reader = new UriReader ();
        Converter converter = new SnakeYamlConverter ();
        // Converter converter = new JacksonConverter ();
        DocumentLoader loader = new DocumentLoader (reader, converter);

        // 2. create a parser.
        DocumentStore documents = new DocumentStore ();
        OpenApiParser parser = new OpenApiParser (documents, loader);

        // 3. parse the OpenAPI from resource or url.
        // here it loads an OpenAPI document from a resource file, but URI works too.
        OpenApiResult result = parser.parse ("openapi.yaml");

        // 4. get the API model from the result to navigate the OpenAPI document.
        // OpenAPI 3.1.x with model.v31.OpenAPI import
        OpenApi model = result.getModel (OpenApi.class);

        // 5. navigate the model
        PathItem pathItem = model.getPaths ().getPathItem ("/foo");

        // 6. create Validator to validate the OpenAPI schema.
        SchemaStore store = new SchemaStore (loader);
        ValidatorSettings settings = new ValidatorSettings ();
        Validator validator = new Validator (settings);

        // 7. validate the OpenAPI schema.
        boolean valid = result.validate (validator, store);

        // 8. print validation errors
        Collection<ValidationError> errors = result.getValidationErrors ();
        ValidationErrorTextBuilder builder = new ValidationErrorTextBuilder ();

        for (ValidationError error : errors) {
            System.out.println (builder.getText(error));
        }
    }
}
