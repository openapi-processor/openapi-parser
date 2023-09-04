/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.converter.*;
import io.openapiprocessor.jsonschema.support.Null;
import io.openapiprocessor.jsonschema.support.Types;
import io.openapiprocessor.jsonschema.support.Uris;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiprocessor.jsonschema.schema.Keywords.*;

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
        return schemaObject.hasProperty (REF);
    }

    @Override
    public @Nullable URI getRef () {
        return schemaObject.convert (REF, new UriConverter ());
    }

    @Override
    public @Nullable String getAnchor () {
        return schemaObject.convert (ANCHOR, new StringNullableConverter ());
    }

    @Override
    public boolean isDynamicRef () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            return schemaObject.hasProperty (RECURSIVE_REF);
        }

        return schemaObject.hasProperty (DYNAMIC_REF);
    }

    @Override
    public @Nullable URI getDynamicRef () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            return schemaObject.convert (RECURSIVE_REF, new UriConverter ());
        }

        return schemaObject.convert (DYNAMIC_REF, new UriConverter ());
    }

    @Override
    public @Nullable String getDynamicAnchor () {
        if (context.getVersion () == SchemaVersion.Draft201909) {
            Boolean anchor = schemaObject.convert (RECURSIVE_ANCHOR, new BooleanConverter ());
            if (anchor == null || !anchor)
                return null;

            return HASH;
        }

        return schemaObject.convert (DYNAMIC_ANCHOR, new StringNullableConverter ());
    }

    @Override
    public JsonSchema getRefSchema () {
        Reference reference = context.getReference (Null.nonNull(getRef()));
        JsonSchemaContext refContext = context.withScope (reference.getValueScope ());

        JsonSchema schema = new JsonSchemaRefConverter (refContext)
            .convert (REF, reference.getValue (), reference.getPointer ());
        if (schema == null)
            throw new NoValueException (getLocation ().append (REF));

        return schema;
    }

    // recursiveRef/ref
    public JsonSchema getRefSchema (@Nullable URI dynamicScope) {
        if (dynamicScope == null) {
            // like $ref
            // no ref in registry with scope
            Reference reference = context.getReference (Null.nonNull (getDynamicRef ()));
            JsonSchemaContext refContext = context.withScope (reference.getValueScope ());
            JsonSchema schema = new JsonSchemaRefConverter (refContext)
                .convert (DYNAMIC_REF, reference.getValue (), reference.getPointer ());

            if (schema == null)
                throw new NoValueException (getLocation ().append (DYNAMIC_REF));

            return schema;
        } else {
            URI dynamicRef = Null.nonNull(getDynamicRef ());
            String fragment = dynamicRef.getFragment ();
            dynamicRef = Uris.createUri ("#" + fragment);

            Reference reference = context.getDynamicReference (dynamicRef, dynamicScope);

            JsonSchemaContext refContext = context.withScope (reference.getValueScope ());
            JsonSchema schema = new JsonSchemaRefConverter (refContext)
                .convert (DYNAMIC_REF, reference.getValue (), reference.getPointer ());

            if (schema == null)
                throw new NoValueException (getLocation ().append (DYNAMIC_REF));

            return schema;
        }
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return schemaObject.convert (SCHEMA, new UriConverter ());
    }

    public @Nullable JsonSchema getMetaSchemaSchema () {
        URI metaSchemaUri = getMetaSchema ();
        if (metaSchemaUri == null) {
            return null;
        }

        Reference reference = context.getReference (metaSchemaUri);

        return new JsonSchemaConverter (context)
            .convert (SCHEMA, reference.getValue (), reference.getPointer ());
    }

    @Override
    public @Nullable Vocabularies getVocabulary () {
        return schemaObject.convert (VOCABULARY, new VocabularyConverter (context));
    }

    @Override
    public @Nullable URI getId () {
        if (context.getVersion () == SchemaVersion.Draft4) {
            return schemaObject.convert (ID4, new UriConverter ());
        }

        return schemaObject.convert (ID, new UriConverter ());
    }

    @Override
    public @Nullable Number getMultipleOf () {
        return schemaObject.convert (MULTIPLE_OF, new NumberConverter ());
    }

    @Override
    public @Nullable Number getMaximum () {
        return schemaObject.convert (MAXIMUM, new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMaximumB () {
        Boolean exclusive = schemaObject.convert (EXCLUSIVE_MAXIMUM, new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Number getExclusiveMaximum () {
        return schemaObject.convert (EXCLUSIVE_MAXIMUM, new NumberConverter ());
    }

    @Override
    public @Nullable Number getMinimum () {
        return schemaObject.convert (MINIMUM, new NumberConverter ());
    }

    @Override
    public Boolean getExclusiveMinimumB () {
        Boolean exclusive = schemaObject.convert (EXCLUSIVE_MINIMUM, new BooleanConverter ());
        if (exclusive == null)
            return false;

        return exclusive;
    }

    @Override
    public @Nullable Number getExclusiveMinimum () {
        return schemaObject.convert (EXCLUSIVE_MINIMUM, new NumberConverter ());
    }

    @Override
    public @Nullable Integer getMaxLength () {
        return schemaObject.convert (MAX_LENGTH, new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMinLength () {
        return schemaObject.convert (MIN_LENGTH, new IntegerConverter ());
    }

    @Override
    public @Nullable String getPattern () {
        return schemaObject.convert (PATTERN, new StringNullableConverter ());
    }

    @Override
    public Collection<JsonSchema> getPrefixItems () {
        Collection<JsonSchema> prefixItems = getJsonSchemasOf (PREFIX_ITEMS);
        if (prefixItems == null)
            return Collections.emptyList ();

        return prefixItems;
    }

    @Override
    public JsonSchemas getItems () {
        boolean exists = schemaObject.hasProperty (ITEMS);
        if (!exists)
            return new JsonSchemas();

        Object raw = schemaObject.getRawValue (ITEMS);
        if (Types.isSchema (raw)) {
            return new JsonSchemas (getJsonSchemaOf (ITEMS));

        } else if (Types.isArray (raw)) {
            Collection<JsonSchema> items = getJsonSchemasOf (ITEMS);
            if (items != null) {
                return new JsonSchemas (items);
            }
        }

        throw new InvalidPropertyException (getLocation ().append (ITEMS));
    }

    @Override
    public JsonSchemas getAdditionalItems () {
        boolean exists = schemaObject.hasProperty (ADDITIONAL_ITEMS);
        if (!exists)
            return new JsonSchemas();

        Object raw = schemaObject.getRawValue (ADDITIONAL_ITEMS);
        if (raw == null)
            return new JsonSchemas ((JsonSchema) null);

        else
            return new JsonSchemas (getJsonSchemaOf (ADDITIONAL_ITEMS));
    }

    public @Nullable JsonSchema getUnevaluatedItems () {
        return getJsonSchemaOf (UNEVALUATED_ITEMS);
    }

    @Override
    public @Nullable Integer getMaxItems () {
        return schemaObject.convert (MAX_ITEMS, new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMinItems () {
        return schemaObject.convert (MIN_ITEMS, new IntegerConverter ());
    }

    @Override
    public @Nullable Boolean isUniqueItems () {
        return schemaObject.convert (UNIQUE_ITEMS, new BooleanConverter ());
    }

    public @Nullable JsonSchema getContains () {
        return getJsonSchemaOf (CONTAINS);
    }

    public Integer getMinContains () {
        Integer minContains = schemaObject.convert (MIN_CONTAINS, new IntegerConverter ());
        if (minContains == null)
            return 1;

        return minContains;
    }

    public @Nullable Integer getMaxContains () {
        return schemaObject.convert (MAX_CONTAINS, new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMaxProperties () {
        return schemaObject.convert (MAX_PROPERTIES, new IntegerConverter ());
    }

    @Override
    public @Nullable Integer getMinProperties () {
        return schemaObject.convert (MIN_PROPERTIES, new IntegerConverter ());
    }

    @Override
    public @Nullable Collection<String> getRequired () {
        Object raw = schemaObject.getRawValue (REQUIRED);
        if (raw == null)
            return null;

        return Collections.unmodifiableCollection(Types.asCol(raw));
    }

    public Map<String, JsonSchema> getProperties () {
        Map<String, JsonSchema> properties = schemaObject.convert (PROPERTIES, new MapJsonSchemasConverter (context));
        if (properties == null)
            return Collections.emptyMap ();

        return properties;
    }

    @Override
    public Map<String, JsonSchema> getPatternProperties () {
        // todo escape regex \
        Map<String, JsonSchema> patternProperties = schemaObject.convert (
            PATTERN_PROPERTIES, new MapJsonSchemasConverter (context));

        if (patternProperties == null) {
            return Collections.emptyMap ();
        }

        return patternProperties;
    }

    @Override
    public @Nullable JsonSchema getAdditionalProperties () {
        return getJsonSchemaOf (ADDITIONAL_PROPERTIES);
    }

    @Override
    public @Nullable JsonSchema getUnevaluatedProperties () {
        return getJsonSchemaOf (UNEVALUATED_PROPERTIES);
    }

    @Override
    public @Nullable Map<String, JsonDependency> getDependencies () {
        return schemaObject.convert (DEPENDENCIES, new MapDependencyConverter (context));
    }

    @Override
    public @Nullable Map<String, JsonSchema> getDependentSchemas () {
        return schemaObject.convert (DEPENDENT_SCHEMAS, new MapJsonSchemasConverter (context));
    }

    public @Nullable Map<String, Set<String>> getDependentRequired () {
        return schemaObject.convert (DEPENDENT_REQUIRED, new MapSetStringsOrEmptyConverter (ResponseType.Null));
    }

    @Override
    public @Nullable JsonSchema getPropertyNames () {
        return getJsonSchemaOf (PROPERTY_NAMES);
    }

    @Override
    public @Nullable JsonSchema getJsonSchema (String property) {
        return schemaProperties.convert (property, new JsonSchemaConverter (context));
    }

    @Override
    public Collection<JsonInstance> getEnum () {
        Object raw = schemaObject.getRawValue (ENUM);
        if (raw == null)
            return  Collections.emptyList ();

        else if (raw instanceof Collection) {
            List<JsonInstance> instances = new ArrayList<> ();

            int index = 0;
            for (Object o : Types.asCol (raw)) {
                JsonInstance instance = new JsonInstance (getLocation ().append (ENUM).append (index), o);
                instances.add (instance);
                index++;
            }

            return Collections.unmodifiableCollection(instances);
        }

        // todo
        throw new RuntimeException ();
    }

    @Override
    public @Nullable JsonInstance getConst () {
        if (!schemaObject.hasProperty (CONST)) {
            return null;
        }

        Object raw = schemaObject.getRawValue (CONST);
        return new JsonInstance (getLocation ().append (CONST), raw);
    }

    @Override
    public Collection<String> getType () {
        boolean exists = schemaObject.hasProperty (TYPE);
        if (!exists)
            return Collections.emptyList ();

        Object raw = schemaObject.getRawValue (TYPE);
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
        Collection<JsonSchema> allOf = getJsonSchemasOf (ALL_OF);
        if (allOf == null)
            return Collections.emptyList ();

        return allOf;
    }

    @Override
    public Collection<JsonSchema> getAnyOf () {
        Collection<JsonSchema> anyOf = getJsonSchemasOf (ANY_OF);
        if (anyOf == null)
            return Collections.emptyList ();

        return anyOf;
    }

    @Override
    public Collection<JsonSchema> getOneOf () {
        Collection<JsonSchema> oneOf = getJsonSchemasOf (ONE_OF);
        if (oneOf == null)
            return Collections.emptyList ();

        return oneOf;
    }

    @Override
    public @Nullable JsonSchema getNot () {
        return getJsonSchemaOf (NOT);
    }

    @Override
    public @Nullable String getFormat () {
        return schemaObject.convert (FORMAT, new StringNullableConverter ());
    }

    @Override
    public @Nullable JsonSchema getIf () {
        return getJsonSchemaOf (IF);
    }

    @Override
    public @Nullable JsonSchema getThen () {
        return getJsonSchemaOf (THEN);
    }

    @Override
    public @Nullable JsonSchema getElse () {
        return getJsonSchemaOf (ELSE);
    }

    @Override
    public String toString () {
        return String.format ("%s", schemaObject.getLocation ());
    }

    private @Nullable JsonSchema getJsonSchemaOf (String property) {
        return schemaObject.convert (property, new JsonSchemaConverter (context));
    }

    private @Nullable Collection<JsonSchema> getJsonSchemasOf (String property) {
        return schemaObject.convert (property, new JsonSchemasConverter (context));
    }

    private Bucket getBucketProperties (Bucket schemaBucket) {
        Bucket bucket = schemaBucket.convert (PROPERTIES, new BucketConverter (schemaBucket));
        if (bucket == null)
            return Bucket.empty ();

        return bucket;
    }
}
