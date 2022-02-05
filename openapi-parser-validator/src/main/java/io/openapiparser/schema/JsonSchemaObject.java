/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;

public class JsonSchemaObject implements JsonSchema {
    private JsonSchemaContext context;  // todo final

    private final Bucket object;
    private final Bucket properties;

    public JsonSchemaObject (Map<String, Object> document) {
        object = new Bucket (document);
        properties = getProperties ();
    }

    public JsonSchemaObject (Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
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
    public boolean isRef () {
        return object.hasProperty ("$ref");
    }

    @Override
    public URI getRef () {
        return object.convert ("$ref", new UriConverter ());
    }

    @Override
    public JsonSchema getRefSchema () {
//        context.
        return null;
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
    public @Nullable Number getMinimum () {
        return object.convert ("minimum", new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMinimum () {
        Boolean exclusive = object.convert ("exclusiveMinimum", new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Integer getMaxLength () {
        return object.convert ("maxLength", new IntegerConverter ());
    }

    @Override
    public Integer getMinLength () {
        final Integer minLength = object.convert ("minLength", new IntegerConverter ());
        if (minLength == null)
            return 0;

        return minLength;
    }

    @Override
    public @Nullable String getPattern () {
        return object.convert ("pattern", new StringNullableConverter ());
    }

    @Override
    public JsonSchemas getItems () {
        boolean exists = object.hasProperty ("items");
        if (!exists)
            return new JsonSchemas();

        Object raw = object.getRawValue ("items");
//        if (raw == null)
//            return new JsonSchemas ((JsonSchema) null);

        if (raw instanceof Map) {
            return new JsonSchemas (getJsonSchemaOf ("items"));

        } else if (raw instanceof Collection) {
            return new JsonSchemas (getJsonSchemasOf ("items"));
        }

        // todo
        throw new RuntimeException ();
    }

    @Override
    public JsonSchemas getAdditionalItems () {
        boolean exists = object.hasProperty ("additionalItems");
        if (!exists)
            return new JsonSchemas();

        Object raw = object.getRawValue ("additionalItems");
        if (raw == null)
            return new JsonSchemas ((JsonSchema) null);

        else
            return new JsonSchemas (getJsonSchemaOf ("additionalItems"));
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

    @Override
    public Collection<String> getType () {
        boolean exists = object.hasProperty ("type");
        if (!exists)
            return Collections.emptyList ();

        Object raw = object.getRawValue ("type");
        if (raw == null)
            return  Collections.singletonList (null);

        else if (raw instanceof String) {
            String type = convert (null, raw, String.class);
            return Collections.singletonList (type);
        }

        else if (raw instanceof Collection) {
            return Collections.unmodifiableCollection(asCol(raw));
        }

        // todo
        throw new RuntimeException ();
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
