/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.*;

import java.net.URI;
import java.util.*;

public interface JsonSchema {

    /**
     * value if the schema is a {@link JsonSchemaBoolean}.
     *
     * @return true or false
     */
    // @JsonSchemaBooleanOnly
    default boolean getBoolean () {
        throw new RuntimeException (); // todo
    }

    // @JsonSchemaBooleanOnly
    default boolean isTrue () {
        throw new RuntimeException (); // todo
    }

    // @JsonSchemaBooleanOnly
    default boolean isFalse () {
        throw new RuntimeException (); // todo
    }

    default boolean isRef () {
        return false;
    }

    default URI getRef () {
        throw new RuntimeException (); // todo
    }

    default JsonSchema getRefSchema () {
        throw new RuntimeException (); // todo
    }

    default @Nullable URI getMetaSchema () {
        return null;
    }

    // todo nullable??
    default URI getId () {
        return null;
    }

    default @Nullable Number getMultipleOf () {
        return null;
    }

    default @Nullable Number getMaximum () {
        return null;
    }

    default Boolean getExclusiveMaximum () {
        return false; // default false (draft4)
    }

    default @Nullable Number getMinimum () {
        return null;
    }

    default Boolean getExclusiveMinimum () {
        return false;  // default false (draft4)
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
        throw new RuntimeException ();
    }

    default Integer getMinItems () {
        return 0;
    }

    default boolean isUniqueItems () {
        return false;
    }

    default @Nullable Integer getMaxProperties () {
        throw new RuntimeException ();
    }

    default Map<String, JsonSchema> getProperties () {
        return Collections.emptyMap ();
    }

    default Map<String, JsonSchema> getPatternProperties () {
        return Collections.emptyMap ();
    }

    default JsonSchema getAdditionalProperties () {
        return new JsonSchemaObject (Collections.emptyMap ());
    }

    default @Nullable JsonSchema getJsonSchema (String propName) {
        return null;
    }

    default Collection<String> getType () {
        return Collections.emptyList ();
    }
}
