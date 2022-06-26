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
 * validates additionalItems and items.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.9">
 *     items</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.10">
 *     additionalItems</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-4.2">
 *     array elements</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.3.1">
 *     additionalItems and items</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-8.2">
 *     array elements</a>
 */
public class Items {
    private final Validator validator;

    public Items (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, Annotations annotations, DynamicScope dynamicScope) {
        CompositeStep step = new FlatStep ();

        ItemsStep itemsStep = new ItemsStep ("items");
        ItemsStep additionalItemsStep = new ItemsStep ("additionalItems");
        ItemsStep unevaluatedItemsStep = new ItemsStep ("unevaluatedItems");

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
                    itemsStep.add (validator.validate (itemsSchema, value, dynamicScope));
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
                            itemsStep.add (validator.validate (itemSchemas.next (), value, dynamicScope));
                        }
                    });

                itemsAnnotation = maxIdx;
            }
            else if (additional.size () == 1) { // isSingle?
                Iterator<JsonSchema> itemSchemas = items.getSchemas ().iterator ();
                JsonSchema additionalSchema = additional.getSchema ();

                if (isBooleanFalse (additionalSchema) && instanceSize > items.size ()) {
                    ItemsSizeStep sStep = new ItemsSizeStep (additionalSchema, instance);
                    sStep.setInvalid ();
                    itemsStep.add (sStep);
                }

                AtomicInteger cntItem = new AtomicInteger ();
                AtomicInteger cntAdditionalItem = new AtomicInteger ();

                IntStream.range (0, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);

                        if (idx < items.size ()) {
                            itemsStep.add (validator.validate (itemSchemas.next (), value, dynamicScope));
                            cntItem.getAndIncrement ();

                        } else {
                            additionalItemsStep.add (validator.validate (additionalSchema, value, dynamicScope));
                            cntAdditionalItem.getAndIncrement ();
                        }
                    });

                itemsAnnotation = cntItem.get ();
                additionalItemsAnnotation = cntAdditionalItem.get () > 0;
            }
        }

        JsonSchema unevaluatedSchema = schema.getUnevaluatedItems ();
        if (unevaluatedSchema != null) {
            Integer allItemsAnnotation = reduceItemsAnnotations (itemsAnnotation, annotations, instanceSize);
            Boolean allAdditionalItemsAnnotations = reduceAdditionalItemsAnnotations (additionalItemsAnnotation, annotations);
            Boolean allUnevaluatedItemsAnnotations = reduceUnevaluatedItemsAnnotations (unevaluatedItemsAnnotation, annotations);

            AtomicInteger cntUnevaluatedItems = new AtomicInteger ();

            if (
                allItemsAnnotation != null && instanceSize > allItemsAnnotation
                && allAdditionalItemsAnnotations == null
                && allUnevaluatedItemsAnnotations == null
            ) {
                IntStream.range (allItemsAnnotation, instanceSize)
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        unevaluatedItemsStep.add (validator.validate (unevaluatedSchema, value, dynamicScope));
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
                        unevaluatedItemsStep.add (validator.validate (unevaluatedSchema, value, dynamicScope));
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
            step.add (itemsStep);

        if (additionalItemsStep.isNotEmpty ())
            step.add (additionalItemsStep);

        if (unevaluatedItemsStep.isNotEmpty ())
            step.add (unevaluatedItemsStep);

        return step;
    }

    private Integer reduceItemsAnnotations (
        @Nullable Integer currentItemsAnnotation, Annotations annotations, int instanceSize
    ) {
        Collection<Annotation> otherAnnotations = annotations.getAnnotations ("items");

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

    private Boolean reduceAdditionalItemsAnnotations (
        @Nullable Boolean currentAdditionalItemsAnnotation, Annotations annotations
    ) {
        Collection<Annotation> otherAnnotations = annotations.getAnnotations ("additionalItems");

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

    private Boolean reduceUnevaluatedItemsAnnotations (
        @Nullable Boolean currentUnevaluatedItemsAnnotation, Annotations annotations
    ) {
        Collection<Annotation> otherAnnotations = annotations.getAnnotations ("unevaluatedItems");

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
