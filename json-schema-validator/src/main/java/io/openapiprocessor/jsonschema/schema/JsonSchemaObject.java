/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.*;
import io.openapiprocessor.jsonschema.support.Nullness;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public class JsonSchemaObject implements JsonSchema {
    private final JsonSchemaContext context;

    private final Bucket schemaObject;
    private final Bucket schemaProperties;


    @SuppressWarnings("method.invocation")
    public JsonSchemaObject (Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
        schemaObject = new Bucket (context.getScope (), document);
        schemaProperties = getBucketProperties (schemaObject);
    }

    @SuppressWarnings("method.invocation")
    public JsonSchemaObject (JsonPointer location, Map<String, Object> document, JsonSchemaContext context) {
        this.context = context;
        this.schemaObject = new Bucket (context.getScope (), location, document);
        schemaProperties = getBucketProperties (schemaObject);
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
        return schemaObject.hasProperty (Keywords.REF);
    }

    @Override
    public @Nullable URI getRef () {
        return schemaObject.convert (Keywords.REF, new UriConverter ());
    }

    @Override
    public @Nullable String getAnchor () {
        return schemaObject.convert ("$anchor", new StringNullableConverter ());
    }

    @Override
    public boolean isDynamicRef () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            return schemaObject.hasProperty (Keywords.RECURSIVE_REF);
        }

        return schemaObject.hasProperty (Keywords.DYNAMIC_REF);
    }

    @Override
    public @Nullable URI getDynamicRef () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            return schemaObject.convert (Keywords.RECURSIVE_REF, new UriConverter ());
        }

        return schemaObject.convert (Keywords.DYNAMIC_REF, new UriConverter ());
    }

    @Override
    public @Nullable String getDynamicAnchor () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            Boolean anchor = schemaObject.convert (Keywords.RECURSIVE_ANCHOR, new BooleanConverter ());
            if (anchor == null || !anchor)
                return null;

            return Keywords.HASH;
        }

        return schemaObject.convert ("$dynamicAnchor", new StringNullableConverter ());
    }

    @Override
    public JsonSchema getRefSchema () {
        Reference reference = context.getReference (Nullness.nonNull(getRef()));
        JsonSchemaContext refContext = context.withScope (reference.getValueScope ());

        JsonSchema schema = new JsonSchemaRefConverter (refContext)
            .convert (Keywords.REF, reference.getValue (), reference.getPointer ());
        if (schema == null)
            throw new NoValueException (getLocation ().append (Keywords.REF));

        return schema;
    }

    // recursiveRef/ref
    public JsonSchema getRefSchema (@Nullable URI dynamicScope) {
        if (dynamicScope == null) {
            // like $ref
            // no ref in registry with scope
            Reference reference = context.getReference (Nullness.nonNull (getDynamicRef ()));
            JsonSchemaContext refContext = context.withScope (reference.getValueScope ());
            JsonSchema schema = new JsonSchemaRefConverter (refContext)
                .convert (Keywords.DYNAMIC_REF, reference.getValue (), reference.getPointer ());

            if (schema == null)
                throw new NoValueException (getLocation ().append (Keywords.DYNAMIC_REF));

            return schema;
        } else {
            URI dynamicRef = Nullness.nonNull(getDynamicRef ());
            String fragment = dynamicRef.getFragment ();
            dynamicRef = UriSupport.createUri ("#" + fragment);

            Reference reference = context.getDynamicReference (dynamicRef, dynamicScope);

            JsonSchemaContext refContext = context.withScope (reference.getValueScope ());
            JsonSchema schema = new JsonSchemaRefConverter (refContext)
                .convert (Keywords.DYNAMIC_REF, reference.getValue (), reference.getPointer ());

            if (schema == null)
                throw new NoValueException (getLocation ().append (Keywords.DYNAMIC_REF));

            return schema;
        }
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return schemaObject.convert (Keywords.SCHEMA, new UriConverter ());
    }

    public @Nullable JsonSchema getMetaSchemaSchema () {
        URI metaSchemaUri = getMetaSchema ();
        if (metaSchemaUri == null) {
            return null;
        }

        Reference reference = context.getReference (metaSchemaUri);

        return new JsonSchemaConverter (context)
            .convert (Keywords.SCHEMA, reference.getValue (), reference.getPointer ());
    }

    @Override
    public @Nullable Vocabularies getVocabulary () {
        return schemaObject.convert (Keywords.VOCABULARY, new VocabularyConverter (context));
    }

    @Override
    public @Nullable URI getId () {
        if (context.getVersion () == SchemaVersion.Draft4) {
            return schemaObject.convert (Keywords.ID4, new UriConverter ());
        }

        return schemaObject.convert (Keywords.ID, new UriConverter ());
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
    public Boolean getExclusiveMinimumB () {
        Boolean exclusive = schemaObject.convert ("exclusiveMinimum", new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Number getExclusiveMinimum () {
        return schemaObject.convert ("exclusiveMinimum", new NumberConverter ());
    }

    @Override
    public @Nullable Integer getMaxLength () {
        return schemaObject.convert ("maxLength", new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMinLength () {
        final Integer minLength = schemaObject.convert ("minLength", new IntegerConverter ());
        if (minLength == null)
            return null;

        return minLength;
    }

    @Override
    public @Nullable String getPattern () {
        return schemaObject.convert ("pattern", new StringNullableConverter ());
    }

    @Override
    public Collection<JsonSchema> getPrefixItems () {
        Collection<JsonSchema> prefixItems = getJsonSchemasOf ("prefixItems");
        if (prefixItems == null)
            return Collections.emptyList ();

        return prefixItems;
    }

    @Override
    public JsonSchemas getItems () {
        boolean exists = schemaObject.hasProperty ("items");
        if (!exists)
            return new JsonSchemas();

        Object raw = schemaObject.getRawValue ("items");
        if (Types.isSchema (raw)) {
            return new JsonSchemas (getJsonSchemaOf ("items"));

        } else if (Types.isArray (raw)) {
            Collection<JsonSchema> items = getJsonSchemasOf ("items");
            if (items != null) {
                return new JsonSchemas (items);
            }
        }

        throw new InvalidPropertyException (getLocation ().append ("items"));
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

    public @Nullable JsonSchema getUnevaluatedItems () {
        return getJsonSchemaOf ("unevaluatedItems");
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

    public @Nullable JsonSchema getContains () {
        return getJsonSchemaOf ("contains");
    }

    public Integer getMinContains () {
        Integer minContains = schemaObject.convert ("minContains", new IntegerConverter ());
        if (minContains == null)
            return 1;

        return minContains;
    }

    public @Nullable Integer getMaxContains () {
        return schemaObject.convert ("maxContains", new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMaxProperties () {
        return schemaObject.convert ("maxProperties", new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMinProperties () {
        return schemaObject.convert ("minProperties", new IntegerConverter ());
    }

    @Override
    public @Nullable Collection<String> getRequired () {
        Object raw = schemaObject.getRawValue ("required");
        if (raw == null)
            return null;

        return Collections.unmodifiableCollection(Types.asCol(raw));
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
    public @Nullable JsonSchema getUnevaluatedProperties () {
        return getJsonSchemaOf ("unevaluatedProperties");
    }

    @Override
    public @Nullable Map<String, JsonDependency> getDependencies () {
        Map<String, JsonDependency> dependencies = schemaObject.convert (
            "dependencies", new MapDependencyConverter (context));

        if (dependencies == null)
            return null;

        return dependencies;
    }

    @Override
    public @Nullable Map<String, JsonSchema> getDependentSchemas () {
        Map<String, JsonSchema> dependentSchemas = schemaObject.convert (
            "dependentSchemas", new MapJsonSchemasConverter (context));

        if (dependentSchemas == null)
            return null;

        return dependentSchemas;
    }

    public @Nullable Map<String, Set<String>> getDependentRequired () {
        Map<String, Set<String>> dependentRequired = schemaObject.convert (
            "dependentRequired", new MapSetStringsOrEmptyConverter (ResponseType.Null));

        if (dependentRequired == null)
            return null;

        return dependentRequired;
    }

    @Override
    public @Nullable JsonSchema getPropertyNames () {
        return getJsonSchemaOf ("propertyNames");
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
            List<JsonInstance> instances = new ArrayList<> ();

            for (Object o : Types.asCol (raw)) {
                Scope scope = context.getScope ().move (o);
                JsonInstance instance = new JsonInstance (
                    o, new JsonInstanceContext (scope, new ReferenceRegistry ())
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
        Scope scope = raw != null ? context.getScope ().move (raw) : context.getScope ();
        return new JsonInstance (raw, new JsonInstanceContext (scope, new ReferenceRegistry ()));
    }

    @Override
    public Collection<String> getType () {
        boolean exists = schemaObject.hasProperty ("type");
        if (!exists)
            return Collections.emptyList ();

        Object raw = schemaObject.getRawValue ("type");
        if (raw instanceof String) {
            String type = Types.convert ("", raw, String.class);
            return Collections.singletonList (type);
        }

        else if (raw instanceof Collection) {
            return Collections.unmodifiableCollection(Types.asCol(raw));
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
    public @Nullable JsonSchema getIf () {
        return getJsonSchemaOf ("if");
    }

    @Override
    public @Nullable JsonSchema getThen () {
        return getJsonSchemaOf ("then");
    }

    @Override
    public @Nullable JsonSchema getElse () {
        return getJsonSchemaOf ("else");
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

    private Bucket getBucketProperties (Bucket schemaBucket) {
        Bucket bucket = schemaBucket.convert ("properties", new BucketConverter (schemaBucket));
        if (bucket == null)
            return Bucket.empty ();

        return bucket;
    }
}
