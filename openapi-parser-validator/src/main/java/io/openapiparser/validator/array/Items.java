/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.Validator;

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
    private final Validator validator;

    public Items (Validator validator) {
        this.validator = validator;
    }

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        int instanceSize = instance.getArraySize ();

        JsonSchemas items = schema.getItems ();
        if (items.isEmpty ()) {
            return messages;

        } else if (items.isNull ()) {
            int i = 0; // todo

        } else if (items.isSingle ()) {
            JsonSchema itemsSchema = items.getSchema ();

            IntStream.range (0, instanceSize)
                .forEach (idx -> {
                    JsonInstance value = instance.getValue (idx);
                    messages.addAll (validator.validate (itemsSchema, value));
                });
        } else {
            JsonSchemas additional = schema.getAdditionalItems ();

            if (additional.isEmpty ()) {
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();

                int maxIdx = instanceSize;
                if (maxIdx > items.size ()) {
                    maxIdx = items.size ();
                }

                IntStream.range (0, maxIdx)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        if (idx < items.size ()) {
                            messages.addAll (validator.validate (itemSchemas.next (), value));
                        }
                    });
            }
            else if (additional.size () == 1) {
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();
                JsonSchema additionalSchema = additional.getSchema ();

                if (isBooleanFalse (additionalSchema) && instanceSize > items.size ()) {
                    messages.add (new ItemsSizeError (instance.getPath (), items.size ()));
                }

                IntStream.range (0, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);

                        if (idx < items.size ()) {
                            messages.addAll (validator.validate (itemSchemas.next (), value));

                        } else {
                            messages.addAll (validator.validate (additionalSchema, value));
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
