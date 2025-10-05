/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.*;

import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

public class OpenApiParser {
    private final DocumentStore documents;
    private final DocumentLoader loader;

    public OpenApiParser (DocumentStore documents, DocumentLoader loader) {
        this.documents = documents;
        this.loader = loader;
    }

    public OpenApiResult parse(URI baseUri) {
        try {
            return parse(baseUri, loader.loadDocument(baseUri));
        } catch (Exception e) {
            throw new ParserException (baseUri, e);
        }
    }

    public OpenApiResult parse(String resource) {
        try {
            return parse(URI.create (resource), loader.loadDocument(resource));
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    public OpenApiResult parse(URI baseUri, Object document) {
        try {
            return parseVersion(baseUri, document);
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    private OpenApiResult parseVersion(URI baseUri, Object document) {
        OpenApiVersion version = OpenApiVersionParser.parseVersion(document);

        if (version == OpenApiVersion.V30) {
            return parse30(baseUri, document);

        } else if (version == OpenApiVersion.V31) {
            return parse31(baseUri, document);

        } else if (version == OpenApiVersion.V32) {
            return parse32(baseUri, document);
        }

        throw new RuntimeException("failed to parser OpenAPI version!");
    }

    private OpenApiResult32 parse32(URI baseUri, Object document) {
        Resolver resolver = new Resolver(documents, loader);
        Resolver.Settings settings = new Resolver.Settings(SchemaVersion.Draft202012)
                .schemaDetector(new OpenApiSchemaDetector())
                .baseUriProvider(new OpenApiBaseUriProvider());

        ResolverResult result = resolver.resolve(baseUri, document, settings);

        return new OpenApiResult32(
                new Context(result.getScope(), result.getRegistry()),
                nonNull(Bucket.createBucket(result.getScope(), document)),
                documents);
    }

    private OpenApiResult31 parse31(URI baseUri, Object document) {
        Resolver resolver = new Resolver(documents, loader);
        Resolver.Settings settings = new Resolver.Settings(SchemaVersion.Draft202012)
                .schemaDetector(new OpenApiSchemaDetector());

        ResolverResult result = resolver.resolve(baseUri, document, settings);

        return new OpenApiResult31(
                new Context(result.getScope(), result.getRegistry()),
                nonNull(Bucket.createBucket(result.getScope(), document)),
                documents);
    }

    private OpenApiResult30 parse30(URI baseUri, Object document) {
        Resolver resolver = new Resolver(documents, loader);

        Resolver.Settings settings = new Resolver.Settings(SchemaVersion.Draft4)
                .schemaDetector(new OpenApiSchemaDetector());

        ResolverResult result = resolver.resolve(baseUri, document, settings);

        return new OpenApiResult30(
                new Context(result.getScope(), result.getRegistry()),
                nonNull(Bucket.createBucket(result.getScope(), document)),
                documents);
    }
}
