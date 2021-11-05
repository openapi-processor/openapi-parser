/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

import java.util.Collection;

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
        refNode = getRefNode ();
    }

    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    @Override
    @Required
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    @Nullable
    public String getSummary () {
        return getSource ().getStringValue (SUMMARY);
    }

    @Nullable
    public String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    @Nullable
    public Operation getGet () {
        return getOperation (GET);
    }

    @Nullable
    public Operation getPut () {
        return getOperation (PUT);
    }

    @Nullable
    public Operation getPost () {
        return getOperation (POST);
    }

    @Nullable
    public Operation getDelete () {
        return getOperation (DELETE);
    }

    @Nullable
    public Operation getOptions () {
        return getOperation (OPTIONS);
    }

    @Nullable
    public Operation getHead () {
        return getOperation (HEAD);
    }

    @Nullable
    public Operation getPatch () {
        return getOperation (PATCH);
    }

    @Nullable
    public Operation getTrace () {
        return getOperation (TRACE);
    }

    public Collection<Server> getServers () {
        return getSource ().getArrayValues (SERVERS, node -> new Server(context, node));
    }

    public Collection<Parameter> getParameters () {
        return getSource ().getArrayValues (PARAMETERS, node -> new Parameter (context, node));
    }

    private Operation getOperation(String property) {
        return getSource ().getObjectValue (property, node -> new Operation (context, node));
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }

    @Nullable
    private Node getRefNode () {
        return context.getRefNodeOrNull (node.getStringValue (REF));
    }
}
