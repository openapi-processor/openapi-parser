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
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#info-object">4.7.2 Info Object</a>
 */
public class Info implements Extensions {
    private final Context context;
    private final Node node;

    public Info (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Required
    public String getTitle () {
        return node.getRequiredStringValue (TITLE);
    }

    public @Nullable String getDescription () {
        return node.getStringValue (DESCRIPTION);
    }

    public @Nullable String getTermsOfService () {
        return node.getStringValue (TERMS_OF_SERVICE);
    }

    public @Nullable Contact getContact () {
        return node.getObjectValue (CONTACT, node -> new Contact (context, node));
    }

    public @Nullable License getLicense () {
        return node.getObjectValue (LICENSE, node -> new License (context, node));
    }

    @Required
    public String getVersion () {
        return node.getRequiredStringValue (VERSION);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
