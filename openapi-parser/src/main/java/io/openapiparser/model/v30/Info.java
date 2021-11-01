/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#info-object">4.7.2 Info Object</a>
 */
public class Info {
    private final Context context;
    private final Node node;

    public Info (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getTitle () {
        return node.getPropertyAsString (TITLE);
    }

    public String getDescription () {
        return node.getPropertyAsString (DESCRIPTION);
    }

    public String getTermsOfService () {
        return node.getPropertyAsString (TERMS_OF_SERVICE);
    }

    public Contact getContact () {
        return node.getPropertyAs (CONTACT, node -> new Contact (context, node));
    }

    public License getLicense () {
        return node.getPropertyAs (LICENSE, node -> new License (context, node));
    }

    public String getVersion () {
        return node.getPropertyAsString (VERSION);
    }
}
