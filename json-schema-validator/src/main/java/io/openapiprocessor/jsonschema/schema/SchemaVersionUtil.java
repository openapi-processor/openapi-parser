package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class SchemaVersionUtil {

    /**
     * Lookup the schema version. Checks the meta schema of the {@code document} first. If the version can not be
     * determined from the meta schema it returns the {@code fallback} version. If there is no meta schema it checks
     * the {@code documentUri}. If the version can not be determined from it, it returns the {@code fallback} version.
     *
     * @param documentUri the document URI
     * @param document the document
     * @param fallback th fallback version
     * @return the schema version or if not available the fallback version
     */
    public static SchemaVersion getSchemaVersion(URI documentUri, Object document, SchemaVersion fallback) {
        URI metaSchema = getMetaSchema(document);
        if (metaSchema != null) {
            return SchemaVersion.getVersion(metaSchema, fallback);
        }

        return SchemaVersion.getVersion(documentUri, fallback);
    }

    /**
     * Lookup the meta schema id. Returns the meta schema uri or null if there is no meta schema.
     *
     * @param document the document
     * @return the meta schema uri or null if unavailable
     */
    public static @Nullable URI getMetaSchema(Object document) {
        Map<String, @Nullable Object> object = Types.asObjectOrNull(document);
        if (object == null) {
            return null;
        }

        Object schema = object.get(Keywords.SCHEMA);
        if (!Types.isString(schema))
            return null;

        return Uris.createUri(Types.asString(schema));
    }
}
