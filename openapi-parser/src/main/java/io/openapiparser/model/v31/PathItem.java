/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

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
        return getOperation (GET);
    }

    public @Nullable Operation getPut () {
        return getOperation (PUT);
    }

    public @Nullable Operation getPost () {
        return getOperation (POST);
    }

    public @Nullable Operation getDelete () {
        return getOperation (DELETE);
    }

    public @Nullable Operation getOptions () {
        return getOperation (OPTIONS);
    }

    public @Nullable Operation getHead () {
        return getOperation (HEAD);
    }

    public @Nullable Operation getPatch () {
        return getOperation (PATCH);
    }

    public @Nullable Operation getTrace () {
        return getOperation (TRACE);
    }

    public Collection<Server> getServers () {
        return getObjectsOrEmpty (SERVERS, Server.class);
    }

    public Collection<Parameter> getParameters () {
        return getObjectsOrEmpty (PARAMETERS, Parameter.class);
    }

    private @Nullable Operation getOperation(String property) {
        return getObjectOrNull (property, Operation.class);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
