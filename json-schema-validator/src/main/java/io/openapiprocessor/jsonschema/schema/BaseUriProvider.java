package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public interface BaseUriProvider {
    /**
     * get a base uri from a document if available.
     *
     * @param documentUri document url
     * @param document    the document
     * @param version     the JSON schema version
     * @return base uri or null
     */
    @Nullable
    URI get(URI documentUri, Object document, SchemaVersion version);
}
