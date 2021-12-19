/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

import java.util.Collection;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Operation</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#operation-object">4.7.10 Operation Object</a>
 */
public class Operation {
    private final Context context;
    private final Node node;

    public Operation (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public Collection<String> getTags () {
        return node.getStringValuesOrEmpty (TAGS);
    }

    @Nullable
    public String getSummary () {
        return node.getStringValue (SUMMARY);
    }

    @Nullable
    public String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    @Nullable
    public ExternalDocumentation getExternalDocs () {
        return node.getObjectValue (EXTERNAL_DOCS, node -> new ExternalDocumentation (context, node));
    }

    @Nullable
    public String getOperationId () {
        return node.getStringValue (OPERATION_ID);
    }

    public Collection<Parameter> getParameters () {
        return node.getObjectValuesOrEmpty (PARAMETERS, node -> new Parameter (context, node));
    }

}
