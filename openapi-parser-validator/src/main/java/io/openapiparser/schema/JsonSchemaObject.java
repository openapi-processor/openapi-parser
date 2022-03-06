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
        properties = getPropertiesX ();
    }

    public JsonSchemaObject (Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
        object = new Bucket (context.getScope (), document);
        properties = getPropertiesX ();
    }

    public JsonSchemaObject (JsonPointer location, Map<String, Object> document, JsonSchemaContext context) {
        this.context = context; // todo check id, switch context
        this.object = new Bucket (context.getScope (), location, document);
        properties = getPropertiesX ();
    }

    private JsonSchemaObject (Bucket object) {
        this.object = object;
        properties = getPropertiesX ();
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
        Reference reference = context.getReference (getRef ());
        Object rawValue = reference.getRawValue ();
        return new JsonSchemaObject(asMap(rawValue), context.withSource (reference.getDocumentUri ()));
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
    public @Nullable Integer getMaxItems () {
        return object.convert ("maxItems", new IntegerConverter ());
    }

    @Override
    public Integer getMinItems () {
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
    public @Nullable Integer getMaxProperties () {
        return object.convert ("maxProperties", new IntegerConverter ());
    }

    @Override
    public Integer getMinProperties () {
        return object.convert ("minProperties", new IntegerConverter ());
    }

    @Override
    public @Nullable Collection<String> getRequired () {
        Object raw = object.getRawValue ("required");
        if (raw == null)
            return null;

        return Collections.unmodifiableCollection(asCol(raw));
    }

    public Map<String, JsonSchema> getProperties () {
        Map<String, JsonSchema> properties = object.convert ("properties", new MapJsonSchemasConverter (context));
        if (properties == null)
            return Collections.emptyMap ();

        return properties;
    }

    @Override
    public Map<String, JsonSchema> getPatternProperties () {
        // todo escape regex \
        Map<String, JsonSchema> patternProperties = object.convert (
            "patternProperties", new MapJsonSchemasConverter (context));

        if (patternProperties == null) {
            return Collections.emptyMap ();
        }

        return patternProperties;
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
        return properties.convert (property, new JsonSchemaConverter (context));
    }

    @Override
    public Collection<JsonInstance> getEnum () {
        Object raw = object.getRawValue ("enum");
        if (raw == null)
            return  Collections.emptyList ();

        else if (raw instanceof Collection) {
            JsonPointer location = object.getLocation ();
            List<JsonInstance> instances = new ArrayList<> ();

            for (Object o : asCol (raw)) {
                JsonInstance instance = new JsonInstance (o, new JsonInstanceContext (
                    location.toUri (), new ReferenceRegistry ())
                );

                instances.add (instance);
            }

            return Collections.unmodifiableCollection(instances);
        }

        // todo
        throw new RuntimeException ();
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

    @Override
    public Collection<JsonSchema> getAllOf () {
        Collection<JsonSchema> allOf = getJsonSchemasOf ("allOf");
        if (allOf == null)
            return Collections.emptyList ();

        return allOf;
    }

    @Override
    public Collection<JsonSchema> getAnyOf () {
        Collection<JsonSchema> anyOf = getJsonSchemasOf ("anyOf");
        if (anyOf == null)
            return Collections.emptyList ();

        return anyOf;
    }

    @Override
    public Collection<JsonSchema> getOneOf () {
        Collection<JsonSchema> oneOf = getJsonSchemasOf ("oneOf");
        if (oneOf == null)
            return Collections.emptyList ();

        return oneOf;
    }

    @Override
    public @Nullable JsonSchema getNot () {
        return getJsonSchemaOf ("not");
    }

    @Override
    public @Nullable String getFormat () {
        return object.convert ("format", new StringNullableConverter ());
    }

    private @Nullable JsonSchema getJsonSchemaOf (String property) {
        return object.convert (property, new JsonSchemaConverter (context));
    }

    private @Nullable Collection<JsonSchema> getJsonSchemasOf (String property) {
        return object.convert (property, new JsonSchemasConverter (context));
    }

    private Bucket getPropertiesX () {
        Bucket bucket = object.convert ("properties", new BucketConverter (object));
        if (bucket == null)
            return Bucket.empty ();

        return bucket;
    }
}
