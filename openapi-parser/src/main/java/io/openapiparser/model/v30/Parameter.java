/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;

/**
 * the <em>Parameter</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#parameter-object">4.7.12 Parameter Object</a>
 */
public class Parameter implements Reference {
    private final Context context;
    private final Node node;
//    private final @Nullable Node refNode;

    public Parameter (Context context, Node node) {
        this.context = context;
        this.node = node;
//        refNode = getRefNode ();
    }

    @Nullable
    @Override
    public String getRef () {
        return null;
    }
}
