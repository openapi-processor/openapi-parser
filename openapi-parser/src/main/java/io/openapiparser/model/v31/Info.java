/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.Context;
import io.openapiparser.Node;

/**
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#info-object">4.8.2 Info Object</a>
 */
public class Info implements Extensions {
    private final Context context;
    private final Node node;

    public Info (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getTitle () {
        return node.getPropertyAsString ("title");
    }

    public String getSummary () {
        return node.getPropertyAsString ("summary");
    }

    public String getDescription () {
        return node.getPropertyAsString ("description");
    }

    public String getTermsOfService () {
        return node.getPropertyAsString ("termsOfService");
    }

    public Contact getContact () {
        return null;
    }

    public License getLicense () {
        return null;
    }

    public String getVersion () {
        return node.getPropertyAsString ("version");
    }
}
