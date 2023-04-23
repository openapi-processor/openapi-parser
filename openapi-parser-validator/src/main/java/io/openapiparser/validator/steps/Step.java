/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.*;

import java.net.URI;
import java.util.stream.Collectors;

import static io.openapiparser.schema.UriSupport.resolve;

/**
 * common step functions.
 */
public class Step {

    public static URI getAbsoluteKeywordLocation (JsonSchema schema) {
        JsonSchemaContext context = schema.getContext ();
        Scope scope = context.getScope ();

        JsonPointer location = schema.getLocation ();
        if (location.isEmpty ()) {
            return scope.getBaseUri ();
        }

        // location may include characters (e.g. regex keys) that require url encoding
        String encoded = "#/" + location.getTokens ()
            .stream ()
            .map (JsonPointerSupport::encode)
            .map (UriSupport::encode)
            .collect (Collectors.joining ("/"));

        return resolve (scope.getBaseUri (),  encoded);
    }

    public static String toString (JsonPointer schema, JsonPointer instance, boolean valid) {
        return String.format ("%s (instance: %s), (schema: %s)", valid ? "valid" : "invalid",
            toString(instance),
            toString(schema));
    }

    private static String toString (JsonPointer pointer) {
        return pointer.isEmpty () ? "/" : pointer.toString ();
    }
}
