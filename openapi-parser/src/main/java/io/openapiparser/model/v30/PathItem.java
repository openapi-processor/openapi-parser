/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#path-item-object">4.7.9 Path Item Object</a>
 */
public class PathItem implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public PathItem (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = context.getRefNodeOrNull (node);
    }

    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    @Override
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    public  @Nullable String getSummary () {
        return getSource ().getStringValue (SUMMARY);
    }

    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    public @Nullable Operation getGet () {
        return getOperation (GET);
    }

    public @Nullable Operation getPut () {
        return getOperation (PUT);
    }

    public @Nullable Operation getPost () {
        return getOperation (POST);
    }

    public @Nullable Operation getDelete () {
        return getOperation (DELETE);
    }

    public @Nullable Operation getOptions () {
        return getOperation (OPTIONS);
    }

    public @Nullable Operation getHead () {
        return getOperation (HEAD);
    }

    public @Nullable Operation getPatch () {
        return getOperation (PATCH);
    }

    public @Nullable Operation getTrace () {
        return getOperation (TRACE);
    }

    public Collection<Server> getServers () {
        return getSource ().getObjectsOrEmpty (SERVERS, node -> new Server(context, node));
    }

    public Collection<Parameter> getParameters () {
        return getSource ().getObjectsOrEmpty (PARAMETERS, node -> new Parameter (context, node));
    }

    private Operation getOperation(String property) {
        return getSource ().getObjectValue (property, node -> new Operation (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
