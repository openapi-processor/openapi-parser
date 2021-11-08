/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;

import java.util.Map;

import static io.openapiparser.Keywords.CONTACT;
import static io.openapiparser.Keywords.LICENSE;

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

    @Required
    public String getTitle () {
        return node.getRequiredStringValue ("title");
    }

    @Nullable
    public String getSummary () {
        return node.getStringValue ("summary");
    }

    @Nullable
    public String getDescription () {
        return node.getStringValue ("description");
    }

    @Nullable
    public String getTermsOfService () {
        return node.getStringValue ("termsOfService");
    }

    @Nullable
    public Contact getContact () {
        return node.getObjectValue (CONTACT, node -> new Contact (context, node));
    }

    @Nullable
    public License getLicense () {
        return node.getObjectValue (LICENSE, node -> new License (context, node));
    }

    @Required
    public String getVersion () {
        return node.getRequiredStringValue ("version");
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }
}
