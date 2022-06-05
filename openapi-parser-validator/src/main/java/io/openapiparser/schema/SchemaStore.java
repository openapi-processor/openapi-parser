/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.validator.Validator;
import io.openapiparser.validator.ValidationMessage;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.openapiparser.converter.Types.asMap;

/**
 * todo this class doesn't really know what it should do...
 */
public class SchemaStore {
    private static final AtomicInteger schemaUriId = new AtomicInteger ();

    private final Map<URI, JsonSchema> schemas = new HashMap<> ();
    Resolver resolver;

    public SchemaStore (Resolver resolver) {
        this.resolver = resolver;
    }

    /**
     * loads a json schema from the given {@code uri} and validates it.
     *
     * @param schemaUri uri of the json schema
     * @return a json schema
     */
    public JsonSchema addSchema (URI schemaUri) {
        return registerSchema (resolver.resolve (schemaUri));
    }

    /**
     * loads a json schema from the classpath {@code resources} and validates it.
     *
     * @param resourcePath path of the json schema in the resources
     * @return a json schema
     */
    public JsonSchema addSchema (String resourcePath) {
        return registerSchema (resolver.resolve (resourcePath));
    }

    /**
     * loads a json schema from the classpath {@code resources} and registers it with the given
     * {@code id}. It does *not* validate the schema.
     *
     * @param id id of the schema
     * @param resourcePath path of the json schema in the resources
     * @return a json schema
     */
    public JsonSchema addSchema (URI id, String resourcePath) {
        ResolverResult result = resolver.resolve (resourcePath);
        JsonSchema schema = createSchema (result);
        schemas.put (id, schema);
        return schema;
    }

    /**
     * registers and validates the given schema.
     *
     * @param schema the raw schema
     * @return a json schema
     */
    public JsonSchema addSchemaDocument (Object schema) {
        ResolverResult result = resolver.resolve (URI.create (""), schema);
        return registerSchema (result);
    }

    /**
     * check if a json schema with the given id is registered.
     *
     * @param id id of the json schema
     * @return true if registered, else false
     */
    public boolean hasSchema (URI id) {
        return schemas.containsKey (id);
    }

    public @Nullable JsonSchema getSchema (URI id) {
        return schemas.get (id);
    }

    public void loadDraft7 () {
        addSchema(SchemaVersion.Draft7.getSchema (), "/json-schema/draft-07/schema.json");
    }

    public void loadDraft6 () {
        addSchema(SchemaVersion.Draft6.getSchema (), "/json-schema/draft-06/schema.json");
    }

    public void loadDraft4 () {
        addSchema(SchemaVersion.Draft4.getSchema (), "/json-schema/draft-04/schema.json");
    }

    private JsonSchema registerSchema (ResolverResult schemaResult) {
        JsonSchema schema = createSchema (schemaResult);

        final URI metaSchemaUri = schema.getMetaSchema ();
        if (metaSchemaUri != null) {
            JsonSchema metaSchema = schemas.get (metaSchemaUri);
            if (metaSchema == null) {
                ResolverResult metaResult = resolver.resolve (metaSchemaUri);
                metaSchema = createSchema (metaResult);
                schemas.put (metaSchemaUri, metaSchema);
            }

            validate (schemaResult, metaSchema);
        }

        // todo no meta schema ?
        // todo use IdProvider
        URI key = schema.getId ();
        if (key == null) {
            key = generateUri ();
        }
        schemas.put (key, schema);
        return schema;
    }

    private void validate (ResolverResult schemaResult, JsonSchema metaSchema) {
        Validator validator = new Validator ();

        JsonInstance instance = new JsonInstance (
            schemaResult.getDocument (),
            createContext (schemaResult));

        Collection<ValidationMessage> messages = validator.validate (metaSchema, instance).getMessages ();
        if (!messages.isEmpty ()) {
            // todo
            throw new RuntimeException ();
        }
    }

    private JsonInstanceContext createContext (ResolverResult schemaResult) {
        return new JsonInstanceContext (schemaResult.getUri (), schemaResult.getRegistry ());
    }

    private URI generateUri () {
        return URI.create (String.format ("schema-%d", schemaUriId.getAndIncrement ()));
    }

    // todo try to auto detect schema version (known uris, known $schema)
    private JsonSchema createSchema (ResolverResult result) {
        Object document = result.getDocument ();

        if (document instanceof Boolean) {
            return new JsonSchemaBoolean (
                (Boolean) document,
                new JsonSchemaContext (result.getUri (), new ReferenceRegistry (), SchemaVersion.Default));

        } else if (document instanceof Map) {

            return new JsonSchemaObject (
                asMap (document),
                new JsonSchemaContext (result.getUri (), result.getRegistry (), SchemaVersion.Default));

        } else {
            // todo
            throw new RuntimeException ();
        }
    }

}
