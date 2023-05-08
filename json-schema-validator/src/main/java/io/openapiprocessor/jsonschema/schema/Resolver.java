/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.Types;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static io.openapiprocessor.jsonschema.schema.Scope.createScope;

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
        private final SchemaVersion version;
        private boolean autoLoadSchemas = false;

        public Settings (SchemaVersion version) {
            this.version = version;
        }

        public Settings autoLoadSchemas (boolean load) {
            this.autoLoadSchemas = load;
            return this;
        }

        public SchemaVersion getVersion () {
            return version;
        }
    }

    private final DocumentStore documents;
    private final DocumentLoader loader;
    private final Settings settings;

    public Resolver (DocumentStore documents, DocumentLoader loader, Settings settings) {
        this.documents = documents;
        this.loader = loader;
        this.settings = settings;
    }

    public ResolverResult resolve (URI uri) {
        try {
            return resolve (uri, loader.loadDocument (uri));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve '%s'.", uri), e);
        }
    }

    public ResolverResult resolve (String resourcePath) {
        try {
            return resolve (URI.create (resourcePath), loader.loadDocument (resourcePath));
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
     * @return resolver result
     *
     * todo try/catch
     */
    public ResolverResult resolve (URI documentUri, Object document) {
        ReferenceRegistry registry = new ReferenceRegistry ();

        documents.addId (documentUri, document);
        Scope scope = Scope.createScope (documentUri, document, settings.version);
        Bucket bucket = toBucket (scope, document);

        if (bucket == null) {
            return new ResolverResult (scope, document, registry, documents);
        }

        ResolverContext context = new ResolverContext (documents, loader, registry);

        ResolverId resolverId = new ResolverId (context);
        resolverId.resolve(bucket);

        ResolverRef resolverRef = new ResolverRef (context);
        resolverRef.resolve(bucket);

        return new ResolverResult (scope, document, registry, documents);
    }

    private @Nullable Bucket toBucket (Scope scope, @PolyNull Object source) {
        if (!Types.isObject (source)) {
            return null;
        }
        return new Bucket (scope, Types.asObject (source));
    }
}
