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

    private final Bucket schemaObject;
    private final Bucket schemaProperties;

    public JsonSchemaObject (Map<String, Object> document) {
        schemaObject = new Bucket (document);
        schemaProperties = getPropertiesX ();
    }

    public JsonSchemaObject (Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
        schemaObject = new Bucket (context.getScope (), document);
        schemaProperties = getPropertiesX ();
    }

    public JsonSchemaObject (JsonPointer location, Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
        this.schemaObject = new Bucket (context.getScope (), location, document);
        schemaProperties = getPropertiesX ();
    }

    @Override
    public JsonSchemaContext getContext () {
        return context;
    }

    public JsonPointer getLocation () {
        return schemaObject.getLocation ();
    }

    @Override
    public boolean isRef () {
        return schemaObject.hasProperty ("$ref");
    }

    @Override
    public URI getRef () {
        return schemaObject.convert ("$ref", new UriConverter ());
    }

    @Override
    public JsonSchema getRefSchema () {
        Reference reference = context.getReference (getRef ());
        JsonSchemaContext refContext = context.withScope (reference.getValueScope ());
        Map<String, Object> props = reference.getValue ();
        return new JsonSchemaObject(reference.getPointer (), props, refContext.withId (props));
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return schemaObject.convert ("$schema", new UriConverter ());
    }

    @Override
    public @Nullable URI getId () {
        return schemaObject.convert ("id", new UriConverter ());
    }

    @Override
    public @Nullable Number getMultipleOf () {
        return schemaObject.convert ("multipleOf", new NumberConverter ());
    }

    @Override
    public @Nullable Number getMaximum () {
        return schemaObject.convert ("maximum", new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMaximumB () {
        Boolean exclusive = schemaObject.convert ("exclusiveMaximum", new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Number getExclusiveMaximum () {
        return schemaObject.convert ("exclusiveMaximum", new NumberConverter ());
    }

    @Override
    public @Nullable Number getMinimum () {
        return schemaObject.convert ("minimum", new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMinimum () {
        Boolean exclusive = schemaObject.convert ("exclusiveMinimum", new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Integer getMaxLength () {
        return schemaObject.convert ("maxLength", new IntegerConverter ());
    }

    @Override
    public Integer getMinLength () {
        final Integer minLength = schemaObject.convert ("minLength", new IntegerConverter ());
        if (minLength == null)
            return 0;

        return minLength;
    }

    @Override
    public @Nullable String getPattern () {
        return schemaObject.convert ("pattern", new StringNullableConverter ());
    }

    @Override
    public JsonSchemas getItems () {
        boolean exists = schemaObject.hasProperty ("items");
        if (!exists)
            return new JsonSchemas();

        Object raw = schemaObject.getRawValue ("items");
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
        boolean exists = schemaObject.hasProperty ("additionalItems");
        if (!exists)
            return new JsonSchemas();

        Object raw = schemaObject.getRawValue ("additionalItems");
        if (raw == null)
            return new JsonSchemas ((JsonSchema) null);

        else
            return new JsonSchemas (getJsonSchemaOf ("additionalItems"));
    }

    @Override
    public @Nullable Integer getMaxItems () {
        return schemaObject.convert ("maxItems", new IntegerConverter ());
    }

    @Override
    public Integer getMinItems () {
        Integer minItems = schemaObject.convert ("minItems", new IntegerConverter ());
        if (minItems == null)
            return 0;

        return minItems;
    }

    @Override
    public boolean isUniqueItems () {
        Boolean unique = schemaObject.convert ("uniqueItems", new BooleanConverter ());
        if (unique == null)
            return false;

        return unique;
    }

    @Override
    public @Nullable Integer getMaxProperties () {
        return schemaObject.convert ("maxProperties", new IntegerConverter ());
    }

    @Override
    public Integer getMinProperties () {
        return schemaObject.convert ("minProperties", new IntegerConverter ());
    }

    @Override
    public @Nullable Collection<String> getRequired () {
        Object raw = schemaObject.getRawValue ("required");
        if (raw == null)
            return null;

        return Collections.unmodifiableCollection(asCol(raw));
    }

    public Map<String, JsonSchema> getProperties () {
        Map<String, JsonSchema> properties = schemaObject.convert ("properties", new MapJsonSchemasConverter (context));
        if (properties == null)
            return Collections.emptyMap ();

        return properties;
    }

    @Override
    public Map<String, JsonSchema> getPatternProperties () {
        // todo escape regex \
        Map<String, JsonSchema> patternProperties = schemaObject.convert (
            "patternProperties", new MapJsonSchemasConverter (context));

        if (patternProperties == null) {
            return Collections.emptyMap ();
        }

        return patternProperties;
    }

    @Override
    public @Nullable JsonSchema getAdditionalProperties () {
        return getJsonSchemaOf ("additionalProperties");
    }

    @Override
    public Map<String, JsonDependency> getDependencies () {
        Map<String, JsonDependency> dependencies = schemaObject.convert (
            "dependencies", new MapDependencyConverter (context));

        if (dependencies == null) {
            return Collections.emptyMap ();
        }

        return dependencies;
    }

    @Override
    public @Nullable JsonSchema getJsonSchema (String property) {
        return schemaProperties.convert (property, new JsonSchemaConverter (context));
    }

    @Override
    public Collection<JsonInstance> getEnum () {
        Object raw = schemaObject.getRawValue ("enum");
        if (raw == null)
            return  Collections.emptyList ();

        else if (raw instanceof Collection) {
            JsonPointer location = schemaObject.getLocation ();
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
    public @Nullable JsonInstance getConst () {
        if (!schemaObject.hasProperty ("const")) {
            return null;
        }

        Object raw = schemaObject.getRawValue ("const");
        return new JsonInstance (raw, new JsonInstanceContext (
            getLocation ().toUri (), new ReferenceRegistry ())
        );
    }

    @Override
    public Collection<String> getType () {
        boolean exists = schemaObject.hasProperty ("type");
        if (!exists)
            return Collections.emptyList ();

        Object raw = schemaObject.getRawValue ("type");
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
        return schemaObject.convert ("format", new StringNullableConverter ());
    }

    @Override
    public String toString () {
        String location = schemaObject.getLocation ().toString ();
        if (location == null) {
            return String.format ("%s", context.getScope ());
        }
        return String.format ("%s", schemaObject.getLocation ());
    }

    private @Nullable JsonSchema getJsonSchemaOf (String property) {
        return schemaObject.convert (property, new JsonSchemaConverter (context));
    }

    private @Nullable Collection<JsonSchema> getJsonSchemasOf (String property) {
        return schemaObject.convert (property, new JsonSchemasConverter (context));
    }

    private Bucket getPropertiesX () {
        Bucket bucket = schemaObject.convert ("properties", new BucketConverter (schemaObject));
        if (bucket == null)
            return Bucket.empty ();

        return bucket;
    }
}
