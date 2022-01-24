/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public interface JsonSchema {

    enum Items {NONE, SINGLE, MULTIPLE}

    default boolean isFalse () {
        return false;
    }

    default @Nullable URI getMetaSchema () {
        return null;
    }

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
        return null;
    }

    default @Nullable Number getMinimum () {
        return null;
    }

    default Boolean getExclusiveMinimum () {
        return null;
    }

    default Items hasItems () {
        return Items.NONE;
    }

    default @Nullable JsonSchema getItems () {
        return null;
    }

    default Collection<JsonSchema> getItemsCollection () {
        return null;
    }

    default @Nullable JsonSchema getAdditionalItems () {
        return null;
    }

    default int getMinItems () {
        return 0;
    }

    default boolean isUniqueItems () {
        return false;
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
}
