/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.converter.StringNotNullConverter;
import io.openapiprocessor.jsonschema.schema.*;

import java.net.URI;

import static io.openapiparser.Keywords.OPENAPI;
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
        String version = getVersion(baseUri, document);

        if (isVersion30(version)) {
            return parse30(baseUri, document);

        } else if (isVersion31(version)) {
            return parse31(baseUri, document);

        } else {
            throw new UnknownVersionException(version);
        }
    }

    private OpenApiResult31 parse31(URI baseUri, Object document) {
        Resolver resolver = new Resolver(documents, loader);
        ResolverResult result = resolver.resolve(baseUri, document, new Resolver.Settings(SchemaVersion.Draft202012));

        return new OpenApiResult31(
                new Context(result.getScope(), result.getRegistry()),
                Bucket.createBucket(result.getScope(), document),
                documents);
    }

    private OpenApiResult30 parse30(URI baseUri, Object document) {
        Resolver resolver = new Resolver(documents, loader);
        ResolverResult result = resolver.resolve(baseUri, document, new Resolver.Settings(SchemaVersion.Draft4));

        return new OpenApiResult30(
                new Context(result.getScope(), result.getRegistry()),
                Bucket.createBucket(result.getScope(), document),
                documents);
    }

    private String getVersion(URI baseUri, Object document) {
        Scope scope = Scope.createScope(baseUri, document, SchemaVersion.getLatest());
        Bucket api = nonNull(Bucket.createBucket(scope, document));
        return  api.convert (OPENAPI, new StringNotNullConverter ());
    }

    private boolean isVersion30(String version) {
        return checkVersion (version, "3.0");
    }

    private boolean isVersion31(String version) {
        return checkVersion (version, "3.1");
    }

    private boolean checkVersion (String version, String prefix) {
        return version.startsWith (prefix);
    }
}
