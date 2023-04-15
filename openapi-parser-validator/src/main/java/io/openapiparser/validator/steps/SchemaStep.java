/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static io.openapiparser.schema.UriSupport.resolve;

public class SchemaStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public SchemaStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        return Collections.singletonList (
            new ValidationMessage (schema, instance,
                "schema", "the schema is invalid", super.getMessages ()));
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return steps.stream ()
            .filter (ValidationStep::isValid)
            .map (s -> s.getAnnotations (keyword))
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }


    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ();
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        JsonSchemaContext context = schema.getContext ();
        Scope scope = context.getScope ();

        JsonPointer location = schema.getLocation ();
        if (location.isEmpty ()) {
            return scope.getBaseUri ();
        }

        return resolve (scope.getBaseUri (), location.toUri ());
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.toString ().isEmpty () ? "/" : instance.toString (),
            schema.toString ().isEmpty () ? "/" : schema.toString ());
    }
}
