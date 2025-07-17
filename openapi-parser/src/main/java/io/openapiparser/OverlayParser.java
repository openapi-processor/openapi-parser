/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.converter.StringNotNullConverter;
import io.openapiprocessor.jsonschema.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

import static io.openapiparser.Keywords.OVERLAY;
import static io.openapiprocessor.jsonschema.support.Null.nonNull;

public class OverlayParser {
    private final DocumentStore documents;
    private final DocumentLoader loader;

    public OverlayParser (DocumentStore documents, DocumentLoader loader) {
        this.documents = documents;
        this.loader = loader;
    }

    public OverlayResult parse(URI baseUri) {
        try {
            return parse(baseUri, loader.loadDocument(baseUri));
        } catch (Exception e) {
            throw new ParserException (baseUri, e);
        }
    }

    public OverlayResult parse(String resource) {
        try {
            return parse(URI.create (resource), loader.loadDocument(resource));
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    public OverlayResult parse(URI baseUri, Object document) {
        try {
            return parseVersion(baseUri, document);
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    private OverlayResult parseVersion(URI baseUri, Object document) {
        String version = getVersion(baseUri, document);

        if (isVersion10(version)) {
            Scope scope = Scope.empty();
            return new OverlayResult10(
                    new Context(scope, new ReferenceRegistry()),
                    nonNull(Bucket.createBucket(scope, document)));
        } else {
            throw new UnknownVersionException(version);
        }
    }

    private @Nullable String getVersion(URI baseUri, Object document) {
        Scope scope = Scope.empty();
        Bucket api = nonNull(Bucket.createBucket(scope, document));
        return  api.convert (OVERLAY, new StringNotNullConverter());
    }

    private boolean isVersion10(@Nullable String version) {
        if (version == null) {
            return false;
        }

        return checkVersion (version, "1.0");
    }

    private boolean checkVersion (String version, String prefix) {
        return version.startsWith (prefix);
    }
}
