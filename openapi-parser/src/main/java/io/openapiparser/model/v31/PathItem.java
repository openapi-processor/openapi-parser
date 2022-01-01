/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#path-item-object">4.8.9 Path Item Object</a>
 */
public class PathItem implements Extensions, Reference {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;
    private final PropertyBucket properties;
    private final PropertyBucket refProperties;

    @Deprecated
    public PathItem (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = context.getRefNodeOrNull (node);
        this.properties = node.toBucket ();
        this.refProperties = context.getRefObjectOrNull (this.properties);
    }

    public PathItem (Context context, PropertyBucket properties) {
        this.context = context;
        this.node = null;
        this.refNode = null;
        this.properties = properties;
        this.refProperties = context.getRefObjectOrNull (this.properties);
    }

    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    @Override
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    @Override
    public @Nullable String getSummary () {
        return getSource ().getStringValue (SUMMARY);
    }

    @Override
    public  @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    public Collection<Server> getServers () {
        return getSource ().getObjectValuesOrEmpty (SERVERS, node -> new Server (context, node));
    }

    public Collection<Parameter> getParameters () {
        return getSource ().getObjectValuesOrEmpty (PARAMETERS, node -> new Parameter (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
