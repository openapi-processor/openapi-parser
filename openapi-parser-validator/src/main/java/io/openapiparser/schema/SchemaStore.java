/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.Converter;
import io.openapiparser.Reader;
import io.openapiparser.support.Strings;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SchemaStore {
    private static final AtomicInteger schemaUriId = new AtomicInteger ();

    private final Reader reader;
    private final Converter converter;

    private Map<URI, JsonSchema> schemas;

    public SchemaStore(Reader reader, Converter converter) {
        this.reader = reader;
        this.converter = converter;
    }

    public JsonSchema addSchemaDocument (Object document) {
        return registerSchema (document);
    }

    /**
     * load json schema from classpath {@code resources}.
     *
     * @param resourcePath path of the json schema in the resources
     * @return a json schema
     */
    public JsonSchema addSchema(String resourcePath) {
        return registerSchema (loadDocument (resourcePath));
    }

    public JsonSchema addSchema (URI documentUri) {
        return registerSchema (loadDocument (documentUri));
    }

    private JsonSchema registerSchema (Object document) {
        JsonSchema schema = createSchema (document);

        final URI metaSchemaUri = schema.getMetaSchema ();
        if (metaSchemaUri != null) {
            JsonSchema metaSchema = schemas.get (metaSchemaUri);
            if (metaSchema == null) {
                 metaSchema = addSchema (metaSchemaUri);
            }

            Validator validator = new Validator ();
            final Collection<ValidationMessage> messages = validator.validate (metaSchema, document);

            if (!messages.isEmpty ()) {
                // todo
                throw new RuntimeException ();
            }
        }

        URI key = schema.getId ();
        if (key == null) {
            key = generateUri ();
        }
        schemas.put (key, schema);
        return schema;
    }

    private Object loadDocument (URI documentUri) throws SchemaStoreException {
        try {
            return converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            // todo
            throw new SchemaStoreException (String.format ("failed to load %s.", documentUri), e);
        }
    }

    private Object loadDocument (String resourcePath) throws SchemaStoreException {
        try {
            return converter.convert (Strings.of (getClass ().getResourceAsStream (resourcePath)));
        } catch (Exception e) {
            // todo
            throw new SchemaStoreException (String.format ("failed to load %s.", resourcePath), e);
        }
    }

    private URI generateUri () {
        return URI.create (String.format ("schema-%d", schemaUriId.getAndIncrement ()));
    }

    @SuppressWarnings ("unchecked")
    private JsonSchema createSchema (Object document) {
        if (document instanceof Boolean) {
            return new JsonSchemaBoolean ((Boolean) document);
        } else if (document instanceof Map) {
            return new JsonSchemaObject ((Map<String, Object>) document);
        } else {
            // todo
            throw new RuntimeException ();
        }
    }

}
