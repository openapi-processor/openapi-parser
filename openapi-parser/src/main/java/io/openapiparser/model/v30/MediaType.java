/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.REF;
import static io.openapiparser.Keywords.SCHEMA;

/**
 * the <em>Media Type</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#media-type-object">4.7.14 Media Type Object</a>
 */
public class MediaType implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public MediaType (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = context.getRefNodeOrNull (node);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    /** {@inheritDoc} */
    @Required
    @Override
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    public @Nullable Schema getSchema () {
        return getSource ().getObjectValue (SCHEMA, node -> new Schema (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
