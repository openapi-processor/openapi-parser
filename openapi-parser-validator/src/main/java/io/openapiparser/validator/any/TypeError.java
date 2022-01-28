/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link Type}.
 */
public class TypeError extends ValidationMessage {
    public TypeError (String path, Collection<String> types) {
        super (path, String.format ("the type should be any of [%s]",
            String.join (", ", types)));
    }
}
