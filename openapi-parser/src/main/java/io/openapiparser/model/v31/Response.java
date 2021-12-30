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
 * the <em>Response</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#response-object">4.8.16 Response Object</a>
 */
public class Response implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Response (Context context, Node node) {
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

    @Override
    public @Nullable String getSummary () {
        return getSource ().getStringValue (SUMMARY);
    }

    @Required
    @Override
    public @Nullable String getDescription () {
        return getSource ().getRequiredStringValue (DESCRIPTION);
    }

    public Map<String, Header> getHeaders () {
        return getSource ().getMapObjectValuesOrEmpty (HEADERS, node -> new Header (context, node));
    }

    public Map<String, MediaType> getContent () {
        return node.getMapObjectValuesOrEmpty (CONTENT, node -> new MediaType (context, node));
    }

    public Map<String, Link> getLinks () {
        return node.getMapObjectValuesOrEmpty (LINKS, node -> new Link (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return getSource ().getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}