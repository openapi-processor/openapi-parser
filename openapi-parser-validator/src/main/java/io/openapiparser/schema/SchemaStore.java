/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.schema.UriSupport.createUri;

/**
 * Schema factory. This is used to register the schemas required to validate a json instant.
 */
public class SchemaStore {
    private static final Logger log = LoggerFactory.getLogger (SchemaStore.class);

    private final Map<URI, JsonSchema> schemaCache = new HashMap<> ();

    private final DocumentStore documents;
    private final DocumentLoader loader;

    public SchemaStore (DocumentLoader loader) {
        this.documents = new DocumentStore ();
        this.loader = loader;
    }

    /**
     * download & register a schema document. {@code schemaUri} should be a json schema document
     * downloadable under the given uri.
     *
     * @param schemaUri schema uri/id
     */
    public void register (URI schemaUri) {
        // check absolute?

        if (documents.contains (schemaUri)) {
            log.warn ("id is already registered: {}", schemaUri.toString ());
            return;
        }

        Object document = loader.loadDocument (schemaUri);
        documents.addId (schemaUri, document);
    }

    /**
     * register a schema document. {@code schemaUri} is schema id of the given {@code document}.
     * The {@code document} must be a {@code Boolean} or a {@code Map<String, Object>}.
     *
     * @param schemaUri schema uri/id
     * @param document the document,
     */
    public void register (URI schemaUri, Object document) {
        // check absolute?

        if (documents.contains (schemaUri)) {
            log.warn ("id is already registered: {}", schemaUri.toString ());
            return;
        }

        documents.addId (schemaUri, document);
    }

    /**
     * register a schema document. Similar to {@code register()} with {@code schemaUri}, except
     * that the {@code schemaUri} gets generated. The {@code document} must be a {@code Boolean} or
     * a {@code Map<String, Object>}.
     *
     * @param document the document
     * @return the generated schema uri
     */
    public URI register (Object document) {
        URI schemaUri = generateUri ();
        documents.addId (schemaUri, document);
        return schemaUri;
    }

    /**
     * register a schema document. {@code resourcePath} should be a json schema document available
     * on the classpath (resource).
     *
     * @param schemaUri schema uri/id
     * @param resourcePath resource path
     */
    public void register (URI schemaUri, String resourcePath) {
        if (documents.contains (schemaUri)) {
            log.warn ("id is already registered: {}", schemaUri.toString ());
            return;
        }

        Object document = loader.loadDocument (resourcePath);
        documents.addId (schemaUri, document);
    }

    /**
     * register draft-201909 json schema.
     */
    public void registerDraft201909 () {
        register (SchemaVersion.Draft201909.getSchemaResource ());
        SchemaVersion.Draft201909.getVocabularyResources ().forEach (this::register);
    }

    /**
     * register draft-7 json schema.
     */
    public void registerDraft7 () {
        register (SchemaVersion.Draft7.getSchemaResource ());
    }

    /**
     * register draft-6 json schema.
     */
    public void registerDraft6 () {
        register (SchemaVersion.Draft6.getSchemaResource ());
    }

    /**
     * register draft-4 json schema.
     */
    public void registerDraft4 () {
        register (SchemaVersion.Draft4.getSchemaResource ());
    }

    private void register (SchemaResource schema) {
        register (schema.getUri (), schema.getResource ());
    }

    /**
     * get a registered json schema. If the schema has no given meta schema it assumes the latest
     * (implemented) json schema draft.
     *
     * @param schemaUri schema id
     * @return the json schema
     */
    public JsonSchema getSchema (URI schemaUri) {
        return getSchema (schemaUri, SchemaVersion.getLatest ());
    }

    /**
     * get a registered json schema. If the schema has no given meta schema it is using the given
     * json schema draft {@code version} as meta schema.
     *
     * @param schemaUri schema id
     * @param version fallback json schema version.
     * @return the json schema
     */
    public JsonSchema getSchema (URI schemaUri, SchemaVersion version) {
        JsonSchema schema = schemaCache.get (schemaUri);
        if (schema != null) {
            return schema;
        }

        Object document = documents.get (schemaUri);
        if (document == null) {
            // todo NotRegisteredException
            throw new RuntimeException ();
        }

        // create schema
        Resolver resolver = new Resolver (documents, loader, new Resolver.Settings (version));
        ResolverResult resolve = resolver.resolve (schemaUri, document);
        schema = createSchema (resolve);

        schemaCache.put (schemaUri, schema);
        return schema;
    }

    private JsonSchema createSchema (ResolverResult result) {
        Scope scope = result.getScope ();
        Object document = result.getDocument ();

        if (isBoolean (document)) {
            Vocabularies vocabularies = Vocabularies.ALL;

            return new JsonSchemaBoolean (
                asBoolean (document),
                new JsonSchemaContext (scope, new ReferenceRegistry (), vocabularies));

        } else if (isObject (document)) {
            URI metaSchema = getMetaSchema (document);
            Vocabularies vocabularies = getVocabularies (metaSchema);

            return new JsonSchemaObject (
                asObject (document),
                new JsonSchemaContext (scope, result.getRegistry (), vocabularies));
        } else {
            // todo
            throw new RuntimeException ();
        }
    }

    private Vocabularies getVocabularies (@Nullable URI metaSchema) {
        if (metaSchema == null) {
            return Vocabularies.ALL;
        }

        SchemaVersion version = SchemaVersion.getVersion (metaSchema);
        if (version != null) {
            return Vocabularies.ALL;
        }

        JsonSchema metaSchemaSchema = getSchema (metaSchema);
        if (metaSchemaSchema == null) {
            // todo throw
            throw new RuntimeException ();
        }

       return metaSchemaSchema.getVocabulary ();
    }

    private @Nullable URI getMetaSchema (Object document) {
        if (!isObject (document))
            return null;

        Map<String, Object> object = asObject (document);
        Object schema = object.get (Keywords.SCHEMA);
        if (!isString (schema))
            return null;

        return createUri (asString(schema));
    }

    private URI generateUri () {
        return URI.create (String.format ("https://%s/", UUID.randomUUID ()));
    }

    public DocumentStore getDocuments () {
        return documents;
    }
}
