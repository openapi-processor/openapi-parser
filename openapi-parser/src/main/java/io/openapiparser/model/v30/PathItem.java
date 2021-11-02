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

    @Nullable
    @Override
    public String getRef () {
        return node.getPropertyAsString (REF);
    }

    @Nullable
    public String getSummary () {
        return getSource ().getPropertyAsString (SUMMARY);
    }

    @Nullable
    public String getDescription () {
        return null;
    }

    @Nullable
    public Operation getGet () {
        return null;
    }

    @Nullable
    public Operation getPut () {
        return null;
    }

    @Nullable
    public Operation getPost () {
        return null;
    }

    @Nullable
    public Operation getDelete () {
        return null;
    }

    @Nullable
    public Operation getOptions () {
        return null;
    }

    @Nullable
    public Operation getHead () {
        return null;
    }

    @Nullable
    public Operation getPatch () {
        return null;
    }

    @Nullable
    public Operation getTrace () {
        return null;
    }

    @Nullable
    public Collection<Server> getServers () {
        return null;
    }

    @Nullable
    public Collection<Parameter> getParameters () {
        return null;
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }

    @Nullable
    private Node getRefNode () {
        String ref = getRef ();
        return ref != null ? context.getRefNode (ref) : null;
    }
}
