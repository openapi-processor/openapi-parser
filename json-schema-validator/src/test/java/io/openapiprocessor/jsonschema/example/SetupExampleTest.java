package io.openapiprocessor.jsonschema.example;

import io.openapiprocessor.interfaces.Converter;
import io.openapiprocessor.interfaces.ConverterException;
import io.openapiprocessor.jackson.JacksonConverter;
import io.openapiprocessor.jsonschema.ouput.OutputConverter;
import io.openapiprocessor.jsonschema.ouput.OutputUnit;
import io.openapiprocessor.jsonschema.reader.UriReader;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Uris.createUri;

public class SetupExampleTest {

    @Test
    void setupExample() throws ConverterException {
        // 1. create a document loader.
        // It loads a document by uri and converts it to a Map<String, Object>
        // object tree that represents the json or yaml content. The Validator
        // operates on that Object tree which makes it independent of the
        // object mapper (e.g. jackson, snakeyaml etc.).
        // Both (Reader and Converter) have a very simple interface which makes
        // it easy to implement your own.
        UriReader reader = new UriReader ();
        Converter converter = new JacksonConverter ();
        DocumentLoader loader = new DocumentLoader (reader, converter);

        // 2. create a json schema store, register a schema and get the schema.
        // the store creates a JsonSchema object from the schema document. A
        // JsonSchema object is a required parameter of the Validator. There are
        // several register() methods and convenience functions to register json
        // schema draft versions (e.g. 2029-09 etc.). Here the store will
        // download the schema and meta schema from the given uri.
        URI schemaUri = createUri ("https://openapiprocessor.io/schemas/mapping/mapping-v4.json");
        SchemaStore store = new SchemaStore (loader);
        //store.registerDraft7();
        store.register(schemaUri);

        // get the json schema object
        JsonSchema schema = store.getSchema (schemaUri);

        // 3. create an instance. An instance is the document we want to validate
        // with the schema. Like the schema above it is a Map<String, Object>
        // object tree. DocumentLoader and converter can be used to create the
        // Map<String, Object> object tree.
        // Here we simply create it from a string using the Converter.
        JsonInstance instance = new JsonInstance (converter.convert (
            "## simple mapping file\n" +
                "\n" +
                "openapi-processor-mapping: v4\n" +
                "options:\n" +
                "  package-name: io.openapiprocessor.generated\n" +
                "  bean-validation: jakarta\n" +
                "  javadoc: true\n" +
                "  model-name-suffix: Resource\n" +
                "  bad: property"
        ));

        // 4. create the validator. The ValidatorSettings are used to enable or
        // disable specific formats or set a fallback schema draft version for
        // schemas that do not provide a meta schema.
        ValidatorSettings settings = new ValidatorSettings ();
        Validator validator = new Validator(settings);

        // 5. run validation. The result ValidationStep is an implementation
        // specific result. It contains all validation details and can be easily
        // converted to the official json schema output format.
        ValidationStep step = validator.validate(schema, instance);
        // boolean valid = step.isValid ();

        // 6. convert to official output format. It supports the formats, flag,
        // basic and verbose.
        OutputConverter output = new OutputConverter(Output.BASIC);
        OutputUnit result = output.convert(step);
        boolean valid = result.isValid ();

        // 7. print errors with error location
        if (!valid && result.getErrors () != null) {
            System.out.println ("validation failed!");

            for (OutputUnit error : result.getErrors ()) {
                String schemaLocation = error.getAbsoluteKeywordLocation ();
                schemaLocation = schemaLocation.substring (schemaLocation.indexOf ('#'));

                String msg = String.format ("%s at instance %s (schema %s)",
                    error.getError (),
                    error.getInstanceLocation (),
                    schemaLocation
                );

               System.out.println (msg);
            }
        }
    }
}
