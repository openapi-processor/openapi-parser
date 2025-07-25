/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#path-item-object">4.8.9 Path Item Object</a>
 */
public class PathItem extends Properties implements Extensions, Reference {

    public PathItem (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    public PathItem getRefObject () {
        return getRefObjectOrThrow (PathItem.class);
    }

    @Override
    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    @Override
    public  @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    public @Nullable Operation getGet () {
        return getOperationOrNull(GET);
    }

    public @Nullable Operation getPut () {
        return getOperationOrNull(PUT);
    }

    public @Nullable Operation getPost () {
        return getOperationOrNull(POST);
    }

    public @Nullable Operation getDelete () {
        return getOperationOrNull(DELETE);
    }

    public @Nullable Operation getOptions () {
        return getOperationOrNull(OPTIONS);
    }

    public @Nullable Operation getHead () {
        return getOperationOrNull(HEAD);
    }

    public @Nullable Operation getPatch () {
        return getOperationOrNull(PATCH);
    }

    public @Nullable Operation getTrace () {
        return getOperationOrNull(TRACE);
    }

    public Map<String, Operation> getOperations () {
        Map<String, Operation> operations = new LinkedHashMap<>();
        bucket.forEach ((operation, value) -> {
            if (OPERATIONS.contains (operation)) {
                operations.put(operation, getOperation (operation));
            }
        });
        return Collections.unmodifiableMap (operations);
    }

    public Collection<Server> getServers () {
        return getObjectsOrEmpty (SERVERS, Server.class);
    }

    public Collection<Parameter> getParameters () {
        return getObjectsOrEmpty (PARAMETERS, Parameter.class);
    }

    private Operation getOperation(String property) {
        return getObjectOrThrow (property, Operation.class);
    }

    private @Nullable Operation getOperationOrNull(String property) {
        return getObjectOrNull (property, Operation.class);
    }

    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
