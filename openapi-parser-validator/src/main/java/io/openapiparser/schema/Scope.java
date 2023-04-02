/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.converter.Types.isObject;
import static io.openapiparser.support.Nullness.nonNull;


// base uri:
// must resolve to absolute (canonical) document uri (no fragment) if no $id is available, else $id

// $id:
// an absolute (canonical) document uri, no fragment (no empty fragment), base uri for relative $refs
public class Scope {
    private final URI documentUri;
    private final URI baseUri;
    private final SchemaVersion version;

    public Scope (URI documentUri, @Nullable URI id, SchemaVersion version) {
        this.documentUri = documentUri;
        baseUri = UriSupport.isEmpty (id) ? documentUri : nonNull(id);
        this.version = version;
    }

    public URI getDocumentUri () {
        return documentUri;
    }

    @Deprecated
    public URI getId () {
        return baseUri;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public SchemaVersion getVersion () {
        return version;
    }

    public Scope resolve (String id) {
        URI resolved = UriSupport.resolve(baseUri, id);
        if (resolved.equals (this.baseUri))
            return this;

        return new Scope (documentUri, resolved, version);
    }

    public Scope resolve (URI id) {
        URI resolved = UriSupport.resolve(baseUri, id);
        if (resolved.equals(this.baseUri))
            return this;

        return new Scope (documentUri, resolved, version);
    }

    public URI resolveAnchor (String anchor) {
        return UriSupport.resolve(baseUri, "#" + anchor);
    }

    public Scope move (URI documentUri, Object document) {
        return createScope (documentUri, document, this);
    }

    public Scope move (Object document) {
        return createScope (baseUri, document, this);
    }

    /**
     * create the scope for the {@code document}. If {@code document} contains an id, it is the
     * base uri, otherwise the scope is the {@code documentUri}. If the {@code documentUri} matches
     * a know json schema the result scope will use its version, otherwise it will use the
     * {@code fallback} version.
     *
     * @param documentUri uri of the document
     * @param document source document
     * @param fallback fallback json schema version
     * @return the scope of the document
     */
    public static Scope createScope (URI documentUri, Object document, SchemaVersion fallback) {
        SchemaVersion version = SchemaVersion.getVersion (documentUri, fallback);

        if (!isObject (document))
            return new Scope (documentUri, null, version);

        IdProvider provider = version.getIdProvider ();
        String id = provider.getId (asMap (document));
        if (id == null) {
            return new Scope (documentUri, null, version);
        }

        return new Scope (documentUri, UriSupport.resolve(documentUri, id), version);
    }

    /**
     * create the scope for the {@code document}. If {@code document} contains an id, it is the
     * base uri, otherwise the scope is the {@code documentUri}. If the {@code documentUri} matches
     * a know json schema the result scope will use its version, otherwise it will use the
     * {@code fallback} version.
     *
     * @param documentUri uri of the document
     * @param document source document
     * @param currentScope the current scope
     * @return the scope of the document
     */
    public static Scope createScope(URI documentUri, Object document, Scope currentScope) {
        SchemaVersion version = SchemaVersion.getVersion (documentUri, currentScope.getVersion ());

        if (!isObject (document))
            return new Scope (documentUri, null, version);

        IdProvider provider = version.getIdProvider ();
        String id = provider.getId (asMap (document));
        if (id == null) {
            return new Scope (documentUri, null, version);
        }

        URI idUri = URI.create (id);
        boolean absolute = idUri.isAbsolute ();
        if (absolute) {
            return new Scope (documentUri, URI.create (id), version);
        } else {
            return new Scope (documentUri, UriSupport.resolve(currentScope.getBaseUri (), id), version);
        }
    }

    @Override
    public String toString () {
        return String.format ("base: %s (%s) (doc: %s)", baseUri, version, documentUri);
    }
}
