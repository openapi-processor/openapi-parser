package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonSchemaObject implements JsonSchema {
    private final Bucket object;
    private final Bucket properties;

    public JsonSchemaObject (Map<String, Object> document) {
        object = new Bucket (JsonPointer.EMPTY, document);
        properties = getProperties ();
    }

    public JsonSchemaObject (JsonPointer location, Map<String, Object> document) {
        this.object = new Bucket (location, document);
        properties = getProperties ();
    }

    private JsonSchemaObject (Bucket object) {
        this.object = object;
        properties = getProperties ();
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return object.convert ("$schema", new UriConverter ());
    }

    @Override
    public @Nullable URI getId () {
        return object.convert ("id", new UriConverter ());
    }

    @Override
    public boolean isUniqueItems () {
        Boolean unique = object.convert ("uniqueItems", new BooleanConverter ());
        if (unique == null)
            return false;

        return unique;
    }

    @Override
    public @Nullable JsonSchema getPropertySchema (String name) {
        return properties.convert (name, new JsonSchemaConverter ());
    }

    private Bucket getProperties () {
        return object.convert ("properties", new PropertyBucketConverter ());
    }
}
