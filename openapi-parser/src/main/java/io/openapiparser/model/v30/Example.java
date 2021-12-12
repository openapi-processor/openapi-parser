/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Example</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#example-object">4.7.19 Example Object</a>
 */
public class Example implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Example (Context context, Node node) {
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

    public  @Nullable String getSummary () {
        return getSource ().getStringValue (SUMMARY);
    }

    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    // todo value, externalValue

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
