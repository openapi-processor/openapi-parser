/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchemaDetector;
import io.openapiprocessor.jsonschema.schema.Scope;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

public class OpenApiSchemaDetector extends JsonSchemaDetector {

    public boolean isJsonSchema(JsonPointer location) {
        JsonPointer parent = location.parent();
        if (parent.matchesParts("components", "schemas")) {
            return true;
        }

        // /paths/<..>/parameters/0/schema
        if (location.matchesParts("paths", null /* path */, "parameters", null /* index */, "schema")) {
            return true;
        }

        // /paths/<..>/<..>/parameters/0/schema
        if (location.matchesParts("paths", null /* path */, null /* method */, "parameters", null /* index */, "schema")) {
            return true;
        }

        // /paths/<..>/<..>/requestBody/content/<mediatype>/schema
        if (location.matchesParts("paths", null /* path */, null /* method */, "requestBody", "content", null /* media type */, "schema")) {
            return true;
        }

        // /paths/<..>/<..>/responses/<status>/content/<mediatype>/schema
        if (location.matchesParts("paths", null /* path */, null /* method */, "responses", null /* status */, "content", null /* media type */, "schema")) {
            return true;
        }

        // /components/responses/<name>/content/<mediatype>/schema
        if (location.matchesParts("components", "responses", null /* name */, "content", null /* media type */, "schema")) {
            return true;
        }

        // /components/parameters/<name>/schema
        if (location.matchesParts("components", "parameters", null /* name */, "schema")) {
            return true;
        }

        // /components/requestBodies/<name>/content/<mediatype>/schema
        if (location.matchesParts("components", "requestBodies", null /* name */, "content", null /* media type */, "schema")) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldWalkObject(Scope scope, @Nullable Object value, JsonPointer location) {
        if (!Types.isObject(value)) {
            return false;
        }

        if (isJsonSchema(location)) {
            return true;
        }

        if (isJsonSchema(location.parent())) {
            return super.shouldWalkObject(scope, value, location);
        }


        // todo check for OpenAPI objects that do not contain schemas to avoid unnecessary walks.
        if (location.matches("/info")) {
            return false;
        }

        return true;
    }

    @Override
    public boolean shouldWalkArray(Scope scope, @Nullable Object value, JsonPointer location) {
        return Types.isArray(value);
    }

    @Override
    public boolean shouldWalkMap(Scope scope, @Nullable Object value, JsonPointer location) {
        return Types.isMap(value);
    }
}
