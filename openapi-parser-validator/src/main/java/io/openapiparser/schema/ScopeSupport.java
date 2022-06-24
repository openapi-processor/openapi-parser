/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;


import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.asMap;

public class ScopeSupport {

    /**
     * calculate new scope if {@code value} is an object a contains an {@code $id}. If there is no
     * {@code $id} it returns the unchanged scope.
     *
     * @param value instance object
     * @param scope current scope
     * @param provider id provider
     * @return new or unchanged scope scope
     */
    public static URI updateScope (Object value, URI scope, IdProvider provider) {
        if (!(value instanceof Map))
            return scope;

        Map<String, Object> props = asMap (value);
        return updateScope (props, scope, provider);
    }


    /**
     * calculate new scope if {@code props} contain an {@code $id}. If there is no {@code $id} it
     * returns the unchanged scope.
     *
     * @param props instance object
     * @param scope current scope
     * @param provider id provider
     * @return new or unchanged scope scope
     */
    public static URI updateScope (Map<String, Object> props, URI scope, IdProvider provider) {
        String id = provider.getId (props);
        if (id != null) {
            scope = scope.resolve (id);
        }
        return scope;
    }
}
