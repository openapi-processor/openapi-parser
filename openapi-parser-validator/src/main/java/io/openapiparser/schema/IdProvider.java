/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.*;

public class IdProvider {
    private enum RefSiblings { YES, NO }

    public static final IdProvider DRAFT201909 = new IdProvider (Keywords.ID, RefSiblings.YES);
    public static final IdProvider DRAFT7 = new IdProvider (Keywords.ID, RefSiblings.NO);
    public static final IdProvider DRAFT6 = new IdProvider (Keywords.ID, RefSiblings.NO);
    public static final IdProvider DRAFT4 = new IdProvider (Keywords.ID4, RefSiblings.NO);

    private final String idKeyword;
    private final RefSiblings refSiblings;

    private IdProvider (String id, RefSiblings siblings) {
        this.idKeyword = id;
        this.refSiblings = siblings;
    }

    /**
     * get the scope from object properties if available.
     *
     * @param properties the object properties
     * @return scope id or null
     */
    public @Nullable String getId (Map<String, Object> properties) {
        Object ref = properties.get (Keywords.REF);
        if (isString (ref) && refSiblings == RefSiblings.NO)
            return null;

        Object id = properties.get (idKeyword);
        if (!isString (id))
            return null;

        return asString (id);
    }
}
