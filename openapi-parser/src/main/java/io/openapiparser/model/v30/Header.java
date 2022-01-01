/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.schema.PropertyBucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Header</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#header-object">4.7.21 Header Object</a>
 */
public class Header implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;
    private final PropertyBucket properties;
    private final @Nullable PropertyBucket refProperties;

    public Header (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = context.getRefNodeOrNull (node);
        this.properties = node.toBucket ();
        this.refProperties = context.getRefObjectOrNull (this.properties);
    }

    public Header (Context context, PropertyBucket properties) {
        this.context = context;
        this.node = null;
        this.refNode = null;
        this.properties = properties;
        this.refProperties = context.getRefObjectOrNull (this.properties);
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


    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    public Boolean getRequired () {
        return getSource ().getBooleanValue (REQUIRED, false);
    }

    public Boolean getDeprecated () {
        return getSource ().getBooleanValue (DEPRECATED, false);
    }

    public Boolean getAllowEmptyValue () {
        return getSource ().getBooleanValue (ALLOW_EMPTY_VALUE, false);
    }

    public String getStyle () {
        String style = getSource ().getStringValue (STYLE);
        if (style != null) {
            return style;
        }

        return "simple";
    }

    public Boolean getExplode () {
        Boolean explode = getSource ().getBooleanValue (EXPLODE);
        if (explode != null) {
            return explode;
        }

        return false;
    }

    public Boolean getAllowReserved () {
        return getSource ().getBooleanValue (ALLOW_RESERVED, false);
    }

    public @Nullable Schema getSchema () {
        return getSource ().getObjectValue (SCHEMA, node -> new Schema (context, node));
    }

    public @Nullable Object getExample () {
        return getSource ().getRawValue (EXAMPLE);
    }

    public Map<String, Example> getExamples () {
        return getSource ().getMapObjectValuesOrEmpty (EXAMPLES, node -> new Example(context, node));
    }

    public Map<String, MediaType> getContent () {
        return getSource ().getMapObjectValuesOrEmpty (CONTENT, node -> new MediaType (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return getSource ().getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
