/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.Validator;

import java.net.URI;
import java.util.*;
import java.util.stream.IntStream;

/**
 * validates additionalItems and items.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.1">
 *     additionalItems and items
 * </a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-8.2">
 *     array elements
 * </a>
 */
public class Items {
    private final URI uri;
    private final JsonSchema schema;
    private final Validator validator;

    public Items (URI uri, JsonSchema schema, Validator validator) {
        this.uri = uri;
        this.schema = schema;
        this.validator = validator;
    }

    public Collection<ValidationMessage> validate (Collection<Object> instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        JsonSchemas items = schema.getItems ();
        if (items.isEmpty ()) {
            return messages;

        } else if (items.isNull ()) {
            // todo
            int i = 0;

        } else if (items.isSingle ()) {
            JsonSchema itemsSchema = items.getSchema ();
            Object[] instanceArray = instance.toArray ();

            IntStream.range (0, instanceArray.length)
                .forEach (idx -> {
                    URI itemUri = JsonPointer.fromJsonPointer (uri.toString ())
                        .append (idx)
                        .toUri ();

                    messages.addAll (validator.validate (itemsSchema, instanceArray[idx], itemUri));
                });
        } else {
            JsonSchemas additional = schema.getAdditionalItems ();

            if (additional.isEmpty ()) {
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();

                int maxIdx = instance.size ();
                if ( instance.size () > items.size ()) {
                    maxIdx = items.size ();
                }

                IntStream.range (0, maxIdx)
                    .forEach (idx -> {
                        URI itemUri = JsonPointer.fromJsonPointer (uri.toString ())
                            .append (idx)
                            .toUri ();

                        if (idx < items.size ()) {
                            messages.addAll (validator.validate (itemSchemas.next (), instance, itemUri));
                        }
                    });
            }
            else if (additional.size () == 1) {
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();
                JsonSchema additionalSchema = additional.getSchema ();

                if (isBooleanFalse (additionalSchema) && instance.size () > items.size ()) {
                    messages.add (new ItemsSizeError (uri.toString (), items.size ()));
                }

                IntStream.range (0, instance.size ())
                    .forEach (idx -> {
                        URI itemUri = JsonPointer.fromJsonPointer (uri.toString ())
                            .append (idx)
                            .toUri ();

                        if (idx < items.size ()) {
                            messages.addAll (validator.validate (itemSchemas.next (), instance, itemUri));

                        } else {
                            messages.addAll (validator.validate (additionalSchema, instance, itemUri));
                        }
                    });
            }
        }
        return messages;
    }

    private boolean isBooleanFalse (JsonSchema schema) {
        return schema instanceof JsonSchemaBoolean && schema.isFalse ();
    }

    private boolean isObject (JsonSchema schema) {
        return schema instanceof JsonSchemaObject;
    }
}
