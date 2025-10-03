/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * loads the base document and resolves all internal and external $ref's. In case of an external
 * $ref it automatically downloads the referenced document if {@code autoLoadSchemas} is enabled.
 * <br>
 * todo make automatic download optional
 * todo prefill documents???
 * todo try to remove version
 * todo just use DocumentStore internally ????
 */
public class Resolver {
    private static final Logger log = LoggerFactory.getLogger (Resolver.class);

    public static class Settings {
        private SchemaVersion version;
        private boolean autoLoadSchemas = false;
        private SchemaDetector schemaDetector = new JsonSchemaDetector();
        private BaseUriProvider baseUriProvider = new JsonBaseUriProvider();

        public Settings(SchemaVersion version) {
            this.version = version;
        }

        public Settings autoLoadSchemas(boolean load) {
            this.autoLoadSchemas = load;
            return this;
        }

        public Settings schemaDetector(SchemaDetector schemaDetector) {
            this.schemaDetector = schemaDetector;
            return this;
        }

        public Settings baseUriProvider(BaseUriProvider baseUriProvider) {
            this.baseUriProvider = baseUriProvider;
            return this;
        }

        public SchemaVersion getVersion () {
            return version;
        }

        public SchemaDetector getSchemaDetector () {
            return schemaDetector;
        }

        public BaseUriProvider getBaseUriProvider () {
            return baseUriProvider;
        }
    }

    private final DocumentStore documents;
    private final DocumentLoader loader;

    public Resolver (DocumentStore documents, DocumentLoader loader) {
        this.documents = documents;
        this.loader = loader;
    }

    /**
     * resolves a given {@code uri}. It will download the document from the given {@code uri} and walk any referenced
     * document. The result contains a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param uri resource path of document
     * @param settings resolver settings
     * @return resolver result
     */
    public ResolverResult resolve (URI uri, Settings settings) {
        try {
            return resolve (uri, loader.loadDocument (uri), settings);
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", uri), e);
        }
    }

    /**
     * resolves a given {@code resourcePath}. It will walk any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param resourcePath resource path of document
     * @param settings resolver settings
     * @return resolver result
     */
    public ResolverResult resolve (String resourcePath, Settings settings) {
        try {
            return resolve (URI.create (resourcePath), loader.loadDocument (resourcePath), settings);
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", resourcePath), e);
        }
    }

    /**
     * resolves a given {@code document}. It will walk any referenced document. The result contains
     * a {@link ReferenceRegistry} that provides the instance of each ref.
     *
     * @param documentUri document uri
     * @param document document content
     * @param settings resolver settings
     * @return resolver result
     */
    public ResolverResult resolve (URI documentUri, Object document, Settings settings) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        documents.addId (documentUri, document);

        DocumentScope documentScope = new DocumentScope(settings.baseUriProvider);
        Scope scope = documentScope.createScope(documentUri, document, settings.version);
        Bucket bucket = Bucket.createBucket(scope, document);

        if (bucket == null) {
            return new ResolverResult (scope, document, registry, documents);
        }

        ResolverContext context = new ResolverContext (documents, loader, registry);

        ResolverId resolverId = new ResolverId (context, settings.getSchemaDetector());
        resolverId.resolve(bucket);

        ResolverRef resolverRef = new ResolverRef (context, resolverId, documentScope);
        resolverRef.resolve(bucket);

        return new ResolverResult (scope, document, registry, documents);
    }

}
