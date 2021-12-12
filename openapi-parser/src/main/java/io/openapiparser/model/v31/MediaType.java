/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Media Type</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#media-type-object">4.8.14 Media Type Object</a>
 */
public class MediaType implements Extensions {
    private final Context context;
    private final Node node;

    public MediaType (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public @Nullable Schema getSchema () {
        return node.getObjectValue (SCHEMA, node -> new Schema (context, node));
    }

    public @Nullable Object getExample () {
        return node.getRawValue (EXAMPLE);
    }

    public Map<String, Example> getExamples () {
        return node.getObjectValuesOrEmpty (EXAMPLES, node -> new Example(context, node));
    }

    public Map<String, MediaType> getContent () {
        return node.getObjectValuesOrEmpty (CONTENT, node -> new MediaType (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
