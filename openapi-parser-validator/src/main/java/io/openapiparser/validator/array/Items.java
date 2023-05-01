/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.*;
import io.openapiparser.validator.steps.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * validates additionalItems and items. Since Draft 4.
 */
public class Items {
    private final Validator validator;

    public Items (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        ItemsStep itemsStep = new ItemsStep (schema, instance, "items");
        ItemsStep additionalItemsStep = new ItemsStep (schema, instance,"additionalItems");
        ItemsStep unevaluatedItemsStep = new ItemsStep (schema, instance,"unevaluatedItems");

        Integer itemsAnnotation = null;
        Boolean additionalItemsAnnotation = null;
        Boolean unevaluatedItemsAnnotation = null;

        int instanceSize = instance.getArraySize ();

        JsonSchemas items = schema.getItems ();
        if (items.isSingle ()) {
            JsonSchema itemsSchema = items.getSchema ();
            IntStream.range (0, instanceSize)
                .forEach (idx -> {
                    JsonInstance value = instance.getValue (idx);
                    validator.validate (itemsSchema, value, dynamicScope, itemsStep);
                });

            itemsAnnotation = instanceSize;
        } else if (items.isArray ()) {
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
                            JsonSchema idxSchema = itemSchemas.next ();
                            SchemaStep idxStep = new SchemaStep (idxSchema, instance);
                            validator.validate (idxSchema, value, dynamicScope, idxStep);
                            itemsStep.add (idxStep);
                        }
                    });

                itemsAnnotation = maxIdx;
            }
            else if (additional.isSingle ()) {
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();
                JsonSchema additionalSchema = additional.getSchema ();

//                if (isBooleanFalse (additionalSchema) && instanceSize > items.size ()) {
//                    ItemsSizeStep sStep = new ItemsSizeStep (additionalSchema, instance);
//                    sStep.setInvalid ();
//                    itemsStep.add (sStep);
//                }

                AtomicInteger cntItem = new AtomicInteger ();
                AtomicInteger cntAdditionalItem = new AtomicInteger ();

                IntStream.range (0, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);

                        if (idx < items.size ()) {
                            JsonSchema idxSchema = itemSchemas.next ();
                            SchemaStep idxStep = new SchemaStep (idxSchema, value);
                            validator.validate (idxSchema, value, dynamicScope, idxStep);
                            itemsStep.add (idxStep);
                            cntItem.getAndIncrement ();

                        } else {
                            SchemaStep idxStep = new SchemaStep (additionalSchema, value);
                            validator.validate (additionalSchema, value, dynamicScope, idxStep);
                            additionalItemsStep.add (idxStep);
                            cntAdditionalItem.getAndIncrement ();
                        }
                    });

                itemsAnnotation = cntItem.get ();
                additionalItemsAnnotation = cntAdditionalItem.get () > 0;
            }
        }

        JsonSchema unevaluatedSchema = schema.getUnevaluatedItems ();
        if (unevaluatedSchema != null) {
            Integer allItemsAnnotation = reduceItemsAnnotations (itemsAnnotation, parentStep, instanceSize);
            Boolean allAdditionalItemsAnnotations = reduceAdditionalItemsAnnotations (additionalItemsAnnotation, parentStep);
            Boolean allUnevaluatedItemsAnnotations = reduceUnevaluatedItemsAnnotations (unevaluatedItemsAnnotation, parentStep);

            AtomicInteger cntUnevaluatedItems = new AtomicInteger ();

            if (
                allItemsAnnotation != null && instanceSize > allItemsAnnotation
                && allAdditionalItemsAnnotations == null
                && allUnevaluatedItemsAnnotations == null
            ) {
                IntStream.range (allItemsAnnotation, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        validator.validate (unevaluatedSchema, value, dynamicScope, unevaluatedItemsStep);
                        cntUnevaluatedItems.getAndIncrement ();
                    });
            } else if (
                allItemsAnnotation == null
                && allAdditionalItemsAnnotations == null
                && allUnevaluatedItemsAnnotations == null
            ) {
                IntStream.range (0, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        validator.validate (unevaluatedSchema, value, dynamicScope, unevaluatedItemsStep);
                        cntUnevaluatedItems.getAndIncrement ();
                    });
            }

            if (cntUnevaluatedItems.get () > 0)
                unevaluatedItemsAnnotation = true;
        }

        if (itemsAnnotation != null) {
            if (itemsAnnotation == instanceSize)
                itemsStep.addAnnotation (true);
            else
                itemsStep.addAnnotation (itemsAnnotation);
        }

        if (additionalItemsAnnotation != null)
            additionalItemsStep.addAnnotation (additionalItemsAnnotation);

        if (unevaluatedItemsAnnotation != null)
            unevaluatedItemsStep.addAnnotation(unevaluatedItemsAnnotation);

        if (itemsStep.isNotEmpty ())
            parentStep.add (itemsStep);

        if (additionalItemsStep.isNotEmpty ())
            parentStep.add (additionalItemsStep);

        if (unevaluatedItemsStep.isNotEmpty ())
            parentStep.add (unevaluatedItemsStep);
    }

    private @Nullable Integer reduceItemsAnnotations (
        @Nullable Integer currentItemsAnnotation, ValidationStep step, int instanceSize
    ) {
        Collection<Annotation> otherAnnotations = step.getAnnotations ("items");

        Integer reducedAnnotation = currentItemsAnnotation;
        if (!otherAnnotations.isEmpty ()) {
            List<Boolean> booleanItemsAnnotations = otherAnnotations
                .stream ()
                .filter (a -> a.is (Boolean.class))
                .map (Annotation::asBoolean)
                .collect (Collectors.toList ());

            List<Integer> integerItemsAnnotations = otherAnnotations
                .stream ()
                .filter (a -> a.is (Integer.class))
                .map (Annotation::asInteger)
                .collect (Collectors.toList ());

            if (currentItemsAnnotation != null) {
                if (currentItemsAnnotation == instanceSize) {
                    booleanItemsAnnotations.add (true);
                } else {
                    integerItemsAnnotations.add (currentItemsAnnotation);
                }
            }

            if (!booleanItemsAnnotations.isEmpty ()) {
                boolean reduced = booleanItemsAnnotations
                    .stream ()
                    .reduce (false, (current, annotation) -> current |= annotation);

                if (reduced)
                    reducedAnnotation = instanceSize; // // i.e. same as true
            } else {
                reducedAnnotation = integerItemsAnnotations
                    .stream()
                    .reduce (0, Math::max);
            }
        }

        return reducedAnnotation;
    }

    private @Nullable Boolean reduceAdditionalItemsAnnotations (
        @Nullable Boolean currentAdditionalItemsAnnotation, ValidationStep step
    ) {
        Collection<Annotation> otherAnnotations = step.getAnnotations ("additionalItems");

        Boolean reducedAnnotation = currentAdditionalItemsAnnotation;
        if (!otherAnnotations.isEmpty ()) {
            List<Boolean> additionalItemsAnnotations = otherAnnotations
                .stream ()
                .filter (a -> a.is (Boolean.class))
                .map (Annotation::asBoolean)
                .collect (Collectors.toList ());

            if (currentAdditionalItemsAnnotation != null) {
                additionalItemsAnnotations.add (currentAdditionalItemsAnnotation);
            }

            reducedAnnotation = additionalItemsAnnotations
                .stream ()
                .reduce (false, (current, annotation) -> current |= annotation);
        }

        return reducedAnnotation;
    }

    private @Nullable Boolean reduceUnevaluatedItemsAnnotations (
        @Nullable Boolean currentUnevaluatedItemsAnnotation, ValidationStep step
    ) {
        Collection<Annotation> otherAnnotations = step.getAnnotations ("unevaluatedItems");

        Boolean reducedAnnotation = currentUnevaluatedItemsAnnotation;
        if (!otherAnnotations.isEmpty ()) {
            List<Boolean> unevaluatedItemsAnnotations = otherAnnotations
                .stream ()
                .filter (a -> a.is (Boolean.class))
                .map (Annotation::asBoolean)
                .collect (Collectors.toList ());

            if (currentUnevaluatedItemsAnnotation != null) {
                unevaluatedItemsAnnotations.add (currentUnevaluatedItemsAnnotation);
            }

            reducedAnnotation = unevaluatedItemsAnnotations
                .stream ()
                .reduce (false, (current, annotation) -> current |= annotation);
        }

        return reducedAnnotation;
    }

    private boolean isBooleanFalse (JsonSchema schema) {
        return schema instanceof JsonSchemaBoolean && schema.isFalse ();
    }

    private boolean isObject (JsonSchema schema) {
        return schema instanceof JsonSchemaObject;
    }
}
