/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Contact</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#contact-object">4.7.3 Contact Object</a>
 */
public class Contact implements Extensions {
    private final Context context;
    private final Node node;

    public Contact (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getName () {
        return node.getString (NAME);
    }

    public String getUrl () {
        return node.getString (URL);
    }

    public String getEmail () {
        return node.getString (EMAIL);
    }
}
