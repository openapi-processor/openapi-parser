/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

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

    @Required
    public String getTitle () {
        return node.getRequiredPropertyAsString (TITLE);
    }

    @Nullable
    public String getDescription () {
        return node.getPropertyAsString (DESCRIPTION);
    }

    @Nullable
    public String getTermsOfService () {
        return node.getPropertyAsString (TERMS_OF_SERVICE);
    }

    @Nullable
    public Contact getContact () {
        return node.getPropertyAs (CONTACT, node -> new Contact (context, node));
    }

    @Nullable
    public License getLicense () {
        return node.getPropertyAs (LICENSE, node -> new License (context, node));
    }

    @Required
    public String getVersion () {
        return node.getRequiredPropertyAsString (VERSION);
    }
}
