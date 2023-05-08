/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiparser.snakeyaml.SnakeYamlConverter;
import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.Reader;
import io.openapiprocessor.jsonschema.reader.UriReader;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.result.Message;
import io.openapiprocessor.jsonschema.validator.result.MessageCollector;
import io.openapiprocessor.jsonschema.validator.result.MessageTextBuilder;

import java.util.LinkedList;

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

        // print validation messages (i.e. errors)
        MessageCollector collector = new MessageCollector (result.getValidationMessages ());
        LinkedList<Message> messages = collector.collect ();
        MessageTextBuilder builder = new MessageTextBuilder ();
        for (Message message : messages) {
            System.out.println (builder.getText(message));
        }
    }
}
