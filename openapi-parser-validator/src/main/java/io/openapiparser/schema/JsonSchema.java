/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;

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

    default boolean isUniqueItems () {
        return false;
    }

    default @Nullable JsonSchema getJsonSchema (String propName) {
        return null;
    }
}
