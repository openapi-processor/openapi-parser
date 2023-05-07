/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Scope;
import io.openapiparser.validator.ValidationMessage;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class SchemaStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public SchemaStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
//        if (shouldFlattenBooleanSchema ()) {
//            assert steps.size () == 1;
//            return steps.stream ()
//                .findFirst ()
//                .get ()
//                .getMessage ();
//        } else {
            return null;
//        }
    }

//    @Override
//    public Collection<ValidationStep> getSteps () {
//        if (shouldFlattenBooleanSchema ()) {
//            return  Collections.emptyList ();
//        } else {
//            return super.getSteps ();
//        }
//    }

//    @Override
//    public Collection<ValidationMessage> getMessages () {
//        return Collections.emptyList ();

//        if (isValid ())
//            return Collections.emptyList ();
//
//        return Collections.singletonList (
//            new ValidationMessage (schema, instance,
//                "schema", "the schema is invalid", super.getMessages ()));
//    }

//    @Override
//    public Collection<Annotation> getAnnotations (String keyword) {
//        return steps.stream ()
//            .filter (ValidationStep::isValid)
//            .map (s -> s.getAnnotations (keyword))
//            .flatMap (Collection::stream)
//            .collect(Collectors.toList ());
//    }

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
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}
