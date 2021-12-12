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
 * the <em>Parameter</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#parameter-object">4.8.12 Parameter Object</a>
 */
public class Parameter implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Parameter (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = getRefNode ();
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
    public String getName () {
        return getSource ().getRequiredStringValue (NAME);
    }

    @Required
    public String getIn () {
        return getSource ().getRequiredStringValue (IN);
    }

    @Nullable
    public String getDescription () {
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

        final String in = getIn ();
        switch (in) {
            case "query":
            case "cookie":
                return "form";
            default:
                return "simple";
        }
    }

    public Boolean getExplode () {
        Boolean explode = getSource ().getBooleanValue (EXPLODE);
        if (explode != null) {
            return explode;
        }

        final String style = getStyle ();
        return "form".equals (style);
    }

    public Boolean getAllowReserved () {
        return getSource ().getBooleanValue (ALLOW_RESERVED, false);
    }

    @Nullable
    public Schema getSchema () {
        return getSource ().getRequiredObjectValue (SCHEMA, node -> new Schema (context, node));
    }

    @Nullable
    public Object getExample () {
        return getSource ().getRawValue (EXAMPLE);
    }

    @Nullable
    public Map<String, Example> getExamples () {
        return getSource ().getObjectValues (EXAMPLES, node -> new Example(context, node));
    }

    @Nullable
    public Map<String, MediaType> getContent () {
        return getSource ().getObjectValues (CONTENT, node -> new MediaType (context, node));
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }

    @Nullable
    private Node getRefNode () {
        return context.getRefNodeOrNull (node);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return null;
    }
}
