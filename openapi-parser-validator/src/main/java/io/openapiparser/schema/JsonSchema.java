/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.*;

import java.net.URI;
import java.util.*;

public interface JsonSchema {

    JsonSchemaContext getContext();
    JsonPointer getLocation ();

    /**
     * value if the schema is a {@link JsonSchemaBoolean}.
     *
     * @return true or false
     */
    // @JsonSchemaBooleanOnly
    default boolean getBoolean () {
        throw new NotImplementedException ();
    }

    // @JsonSchemaBooleanOnly
    default boolean isTrue () {
        throw new NotImplementedException ();
    }

    // @JsonSchemaBooleanOnly
    default boolean isFalse () {
        throw new NotImplementedException ();
    }

    default boolean isRef () {
        return false;
    }

    default @Nullable URI getRef () {
        throw new NotImplementedException ();
    }

    default @Nullable String getAnchor () {
        throw new NotImplementedException ();
    }

    default boolean isDynamicRef () {
        return false;
    }

    default @Nullable URI getDynamicRef () {
        throw new NotImplementedException ();
    }

    default @Nullable String getDynamicAnchor () {
        return null;
    }

    default JsonSchema getRefSchema () {
        throw new NotImplementedException ();
    }

    default JsonSchema getRefSchema (URI scope) {
        throw new NotImplementedException ();
    }

    default @Nullable URI getMetaSchema () {
        return null;
    }

    default @Nullable URI getId () {
        return null;
    }

    default @Nullable Number getMultipleOf () {
        return null;
    }

    default @Nullable Number getMaximum () {
        return null;
    }

    default Boolean getExclusiveMaximumB () {
        return false; // default false (draft4)
    }

    default @Nullable Number getExclusiveMaximum () {
        return null;
    }

    default @Nullable Number getMinimum () {
        return null;
    }

    default Boolean getExclusiveMinimumB () {
        return false;  // default false (draft4)
    }

    default @Nullable Number getExclusiveMinimum () {
        return null;
    }

    default @Nullable Integer getMaxLength () {
        return null;
    }

    default Integer getMinLength () {
        return 0;
    }

    default @Nullable String getPattern () {
        return null;
    }

    default JsonSchemas getItems () {
        return new JsonSchemas ();  // default empty schema (draft 4)
    }

    default JsonSchemas getAdditionalItems () {
        return new JsonSchemas (); // default empty schema (draft 4)
    }

    default @Nullable Integer getMaxItems () {
        return null;
    }

    default Integer getMinItems () {
        return 0;
    }

    default boolean isUniqueItems () {
        return false;
    }

    default @Nullable JsonSchema getContains () {
        return null;
    }

    default @Nullable Integer getMaxProperties () {
        return null;
    }

    default Integer getMinProperties () {
        return 0;
    }

    default @Nullable Collection<String> getRequired () {
        return null;
    }

    default Map<String, JsonSchema> getProperties () {
        return Collections.emptyMap ();
    }

    default Map<String, JsonSchema> getPatternProperties () {
        return Collections.emptyMap ();
    }

    default @Nullable JsonSchema getAdditionalProperties () {
        return null;
    }

    default @Nullable JsonSchema getUnevaluatedProperties () { return null; }

    default Map<String, JsonDependency> getDependencies () {
        return Collections.emptyMap ();
    }

    default Map<String, JsonSchema> getDependentSchemas () {
        return Collections.emptyMap ();
    }

    default @Nullable JsonSchema getPropertyNames () {
        return null;
    }

    default @Nullable JsonSchema getJsonSchema (String propName) {
        return null;
    }

    default Collection<JsonInstance> getEnum () {
        return Collections.emptyList ();
    }

    default @Nullable JsonInstance getConst () {
        return null;
    }

    default Collection<String> getType () {
        return Collections.emptyList ();
    }

    default Collection<JsonSchema> getAllOf () {
        return Collections.emptyList ();
    }

    default Collection<JsonSchema> getAnyOf () {
        return Collections.emptyList ();
    }

    default Collection<JsonSchema> getOneOf () {
        return Collections.emptyList ();
    }

    default @Nullable JsonSchema getNot () {
        return null;
    }

    default @Nullable String getFormat () {
        return null;
    }

    default @Nullable JsonSchema getIf () {
        return null;
    }

    default @Nullable JsonSchema getThen () {
        return null;
    }

    default @Nullable JsonSchema getElse () {
        return null;
    }
}
