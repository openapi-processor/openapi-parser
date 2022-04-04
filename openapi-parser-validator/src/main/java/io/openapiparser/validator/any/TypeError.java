/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.Collection;

/**
 * Created by {@link Type}.
 */
public class TypeError extends ValidationMessage {

    public TypeError (URI scope, String path, Collection<String> types) {
        this (scope.toString (), path, types);
    }

    public TypeError (String scope, String path, Collection<String> types) {
        super (scope, path, String.format ("the type should be any of [%s]",
            String.join (", ", types)));
    }
}
