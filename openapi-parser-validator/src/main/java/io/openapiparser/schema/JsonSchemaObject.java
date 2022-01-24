/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public class JsonSchemaObject implements JsonSchema {
    private final Bucket object;
    private final Bucket properties;

    public JsonSchemaObject (Map<String, Object> document) {
        object = new Bucket (document);
        properties = getProperties ();
    }

    public JsonSchemaObject (JsonPointer location, Map<String, Object> document) {
        this.object = new Bucket (URI.create (""), location, document);
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
    public @Nullable Number getMultipleOf () {
        return object.convert ("multipleOf", new NumberConverter ());
    }

    @Override
    public @Nullable Number getMaximum () {
        return object.convert ("maximum", new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMaximum () {
        Boolean exclusive = object.convert ("exclusiveMaximum", new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public Items hasItems () {
        Object raw = object.getRawValue ("items");
        if (raw == null)
            return Items.NONE;

        if (raw instanceof Map)
            return Items.SINGLE;

        if (raw instanceof Collection)
            return Items.MULTIPLE;

        // todo
        throw new RuntimeException ();
    }

    @Override
    public @Nullable JsonSchema getItems () {
        return getJsonSchema ("items");
    }

    @Override
    public Collection<JsonSchema> getItemsCollection () {
        return getJsonSchemasOf ("items");
    }

    @Override
    public @Nullable JsonSchema getAdditionalItems () {
        return getJsonSchemaOf ("additionalItems");
    }

    @Override
    public int getMinItems () {
        Integer minItems = object.convert ("minItems", new IntegerConverter ());
        if (minItems == null)
            return 0;

        return minItems;
    }

    @Override
    public boolean isUniqueItems () {
        Boolean unique = object.convert ("uniqueItems", new BooleanConverter ());
        if (unique == null)
            return false;

        return unique;
    }

    @Override
    public Map<String, JsonSchema> getPatternProperties () {
        // todo escape regex \
        return object.convert ("patternProperties", new MapJsonSchemasConverter ());
    }

    @Override
    public JsonSchema getAdditionalProperties () {
        final JsonSchema schema = getJsonSchemaOf ("additionalProperties");
        if (schema == null)
            return JsonSchema.super.getAdditionalProperties ();

        return schema;
    }

    @Override
    public @Nullable JsonSchema getJsonSchema (String property) {
        return properties.convert (property, new JsonSchemaConverter ());
    }

    private @Nullable JsonSchema getJsonSchemaOf (String property) {
        return object.convert (property, new JsonSchemaConverter ());
    }

    private @Nullable Collection<JsonSchema> getJsonSchemasOf (String property) {
        return object.convert (property, new JsonSchemasConverter ());
    }

    private Bucket getProperties () {
        Bucket bucket = object.convert ("properties", new BucketConverter (object));
        if (bucket == null)
            return Bucket.empty ();

        return bucket;
    }
}
