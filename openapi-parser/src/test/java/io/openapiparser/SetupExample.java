/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.jackson.JacksonConverter;
import io.openapiparser.model.v30.OpenApi;
import io.openapiparser.reader.UriReader;
import io.openapiparser.schema.*;
import io.openapiparser.snakeyaml.SnakeYamlConverter;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.result.*;

import java.util.LinkedList;

public class SetupExample {

    void parseAndValidate () {
        Reader reader = new UriReader ();
        DocumentStore documents = new DocumentStore ();
        Converter converter = new SnakeYamlConverter ();
        // Converter converter = new JacksonConverter ();
        Resolver resolver = new Resolver (reader, converter, documents);

        // parser OpenAPI file or url
        OpenApiParser parser = new OpenApiParser (resolver);
        OpenApiResult result = parser.parse ("openapi.yaml");
        // OpenAPI 3.1.x with model.v31.OpenAPI import
        OpenApi model = result.getModel (OpenApi.class);

        // validate OpenAPI
        SchemaStore store = new SchemaStore (resolver);
        ValidatorSettings settings = new ValidatorSettings ();
        Validator validator = new Validator (settings);
        boolean valid = result.validate (validator, store);

        // print validation messages (i.e. errors)
        MessageCollector collector = new MessageCollector (result.getValidationMessages ());
        LinkedList<Message> messages = collector.collect ();
        MessageTextBuilder builder = new MessageTextBuilder ();
        for (Message message : messages) {
            System.out.println (builder.getText(message));
        }
    }
}
