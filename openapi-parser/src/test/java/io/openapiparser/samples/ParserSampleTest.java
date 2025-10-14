/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.samples;

import io.openapiparser.*;
import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.Reader;
import io.openapiprocessor.interfaces.Writer;
import io.openapiprocessor.jackson.JacksonJsonWriter;
import io.openapiprocessor.jsonschema.reader.UriReader;
import io.openapiprocessor.jsonschema.schema.DocumentLoader;
import io.openapiprocessor.jsonschema.schema.DocumentStore;
import io.openapiprocessor.jsonschema.schema.SchemaStore;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.snakeyaml.SnakeYamlConverter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserSampleTest {

    /**
     * create a document loader. It loads a document by uri and converts it to a {@code Map<String, Object>}
     * object tree that represents the OpenAPI document. The parser operates on that Object tree which makes
     * it independent of the object mapper (i.e. Jackson or SnakeYAML). Both (Reader and Converter) have a
     * very simple interface which makes it easy to implement your own.
     *
     * @return a document loader
     */
    DocumentLoader createLoader() {
        Reader reader = new UriReader();
        Converter converter = new SnakeYamlConverter();
        // Converter converter = new JacksonConverter ();
        return new DocumentLoader (reader, converter);
    }

    @Test
    void parse() {
        // create the OpenAPI parser.
        OpenApiParser parser = new OpenApiParser(new DocumentStore(), createLoader());

        // parse the OpenAPI from resource or url. Here it loads the OpenAPI document from a resource file.
        OpenApiResult result = parser.parse ("/samples/openapi32.yaml");

        // get the API model from the result to navigate the OpenAPI document. Each OpenAPI version has its
        // own model.
        // To work with multiple versions it is recommended to hide them behind a common interface that
        // provides the properties required by the application. The openapi-parser does currently not provide
        // such an interface.
        OpenApiVersion version = result.getVersion();
        if (version == OpenApiVersion.V31) {
            io.openapiparser.model.v31.OpenApi model = result.getModel (io.openapiparser.model.v31.OpenApi.class);
            // ....

        } else if (version == OpenApiVersion.V32) {
            // get the version specific model
            io.openapiparser.model.v32.OpenApi model = result.getModel (io.openapiparser.model.v32.OpenApi.class);

            // navigate the model
            io.openapiparser.model.v32.Paths paths = model.getPaths ();
            if (paths == null) {
                throw new RuntimeException("missing paths");
            }

            io.openapiparser.model.v32.PathItem pathItem = paths.getPathItem ("/foo");
            if (pathItem == null) {
                throw new RuntimeException("missing path item");
            }

            Map<String, io.openapiparser.model.v32.Operation> operations = pathItem.getOperations();
            // ...
        }
    }

    @Test
    void validate() {
        DocumentLoader loader = createLoader();

        // create the OpenAPI parser.
        OpenApiParser parser = new OpenApiParser(new DocumentStore(), loader);

        // create a JSON schema validator.
        SchemaStore store = new SchemaStore(loader);
        Validator validator = new Validator(new ValidatorSettings());

        // parse the OpenAPI document.
        OpenApiResult result = parser.parse ("/samples/openapi32.yaml");


        // validate the OpenAPI schema.
        boolean valid = result.validate (validator, store);

        // print validation errors.
        Collection<ValidationError> errors = result.getValidationErrors();
        ValidationErrorTextBuilder builder = new ValidationErrorTextBuilder();

        for (ValidationError error : errors) {
            System.out.println(builder.getText(error));
        }
    }

    @Test
    void bundle() throws IOException {
        // create the OpenAPI parser.
        OpenApiParser parser = new OpenApiParser(new DocumentStore(), createLoader());

        // parse the OpenAPI document.
        OpenApiResult result = parser.parse("/samples/openapi32.yaml");

        // bundle it, i.e. inline external $refs.
        Map<String, @Nullable Object> bundled = result.bundle();

        // save the bundled document.
        Writer writer = new JacksonJsonWriter(new FileWriter("./bundle32.yaml"));
        writer.write(bundled);

        // to navigate the bundle it has to be parsed. The bundled object tree has no uri (which is
        // used to cache the document) so we provide one.
        OpenApiResult resultBundled = parser.parse(URI.create("/samples/bundled32.yaml"), bundled);

        // get the version specific model.
        io.openapiparser.model.v32.OpenApi model = resultBundled.getModel (io.openapiparser.model.v32.OpenApi.class);
        // ...
    }

    @Test
    void overlay() {
        DocumentLoader loader = createLoader();
        DocumentStore documents = new DocumentStore();

        // create the OpenAPI parser.
        OpenApiParser openApiParser = new OpenApiParser(documents, loader);

        // create the Overlay parser.
        OverlayParser overlayParser = new OverlayParser(documents, loader);

        // create JSON schema validator.
        SchemaStore store = new SchemaStore(loader);
        Validator validator = new Validator(new ValidatorSettings());

        // parse & validate the OpenAPI document.
        OpenApiResult result = openApiParser.parse("/samples/openapi32.yaml");
        boolean valid = result.validate(validator, store);

        // bundle it, i.e. inline external $refs.
        Map<String, @Nullable Object> bundled = result.bundle();

        // to navigate the bundle it has to be parsed. The bundled object tree has no uri (which is
        // used to cache the document) so we provide one.
        OpenApiResult resultBundled = openApiParser.parse(URI.create("/samples/bundled32.yaml"), bundled);

        // parse & validate the Overlay document.
        OverlayResult resultOverlay = overlayParser.parse("/samples/overlay32.yaml");
        boolean validOverlay = resultOverlay.validate(validator, store);

        // apply Overlay.
        Map<String, @Nullable Object> appliedOverlay = resultBundled.apply(resultOverlay);

        // to navigate the overlay'ed document it has to be parsed. The object tree has no uri (which is
        // used to cache the document) so we provide one.
        OpenApiResult resultApplied = openApiParser.parse(URI.create("/samples/overlayed32.yaml"), appliedOverlay);

        // get the version specific model and check the modification
        io.openapiparser.model.v32.OpenApi model = resultApplied.getModel (io.openapiparser.model.v32.OpenApi.class);
        io.openapiparser.model.v32.Components components = nonNull(model.getComponents());

        Map<String, io.openapiparser.model.v32.Schema> schemas = components.getSchemas();
        schemas.keySet().forEach(key -> {
            io.openapiparser.model.v32.Schema bar = nonNull(schemas.get(key).getProperties().get("bar"));
            String description = bar.getDescription();
            assertEquals("added by overlay!", description);
        });
    }
}
