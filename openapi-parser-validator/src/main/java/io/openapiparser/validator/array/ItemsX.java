/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.Annotations;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.CompositeStep;
import io.openapiparser.validator.steps.FlatStep;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * validates prefixItems and items.
 * <p>Specifications: Since Draft 2020-12
 */
public class ItemsX {
    private final Validator validator;

    public ItemsX (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, Annotations annotations, DynamicScope dynamicScope) {
        CompositeStep step = new FlatStep ();

        ItemsStep prefixItemsStep = new ItemsStep ("prefixItems");
        ItemsStep itemsStep = new ItemsStep ("items");
        ItemsStep unevaluatedItemsStep = new ItemsStep ("unevaluatedItems");

        Integer prefixItemsAnnotation = null;
        Boolean itemsAnnotation = null;
        Boolean unevaluatedItemsAnnotation = null;

        int instanceSize = instance.getArraySize ();

        Collection<JsonSchema> prefixItems = schema.getPrefixItems ();
        if (!prefixItems.isEmpty ()) {
            Iterator<JsonSchema> prefixItemsSchemas = prefixItems.iterator ();

            int maxIdx = instanceSize;
            if (maxIdx > prefixItems.size ()) {
                maxIdx = prefixItems.size ();
            }

            IntStream.range (0, maxIdx)
                .forEach (idx -> {
                    JsonInstance value = instance.getValue (idx);
                    if (idx < prefixItems.size ()) {
                        prefixItemsStep.add (validator.validate (prefixItemsSchemas.next (), value, dynamicScope));
                    }
                });

            prefixItemsAnnotation = maxIdx;
        }

        JsonSchemas items = schema.getItems ();
        if (items.isSingle ()) {
            JsonSchema itemsSchema = items.getSchema ();

            Integer startIndex = prefixItemsAnnotation;
            if (startIndex == null) {
                startIndex = 0;
            }

            AtomicInteger itemsCnt = new AtomicInteger ();
            IntStream.range (startIndex, instanceSize)
                .forEach (idx -> {
                    JsonInstance value = instance.getValue (idx);
                    itemsStep.add (validator.validate (itemsSchema, value, dynamicScope));
                    itemsCnt.getAndIncrement ();
                });

            itemsAnnotation = itemsCnt.get () > 0;
        }

        JsonSchema unevaluatedSchema = schema.getUnevaluatedItems ();
        if (unevaluatedSchema != null) {
            Integer allPrefixItemsAnnotation = reducePrefixItemsAnnotations (prefixItemsAnnotation, annotations, instanceSize);
            Boolean allItemsAnnotation = reduceItemsAnnotations (itemsAnnotation, annotations);
            Collection<Integer> containsAnnotation = getContainsAnnotation(annotations);
            Boolean allUnevaluatedItemsAnnotation = reduceUnevaluatedItemsAnnotations (unevaluatedItemsAnnotation, annotations);

            AtomicInteger cntUnevaluatedItems = new AtomicInteger ();

            if (
                allPrefixItemsAnnotation != null && instanceSize > allPrefixItemsAnnotation
                && allItemsAnnotation == null
                && allUnevaluatedItemsAnnotation == null
            ) {
                IntStream.range (allPrefixItemsAnnotation, instanceSize)
                    .filter (idx -> !containsAnnotation.contains (idx))
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        unevaluatedItemsStep.add (validator.validate (unevaluatedSchema, value, dynamicScope));
                        cntUnevaluatedItems.getAndIncrement ();
                    });
            } else if (
                allPrefixItemsAnnotation == null
                && allItemsAnnotation == null
                && allUnevaluatedItemsAnnotation == null
            ) {
                IntStream.range (0, instanceSize)
                    .filter (idx -> !containsAnnotation.contains (idx))
                    .forEach (idx -> {
                        JsonInstance value = instance.getValue (idx);
                        unevaluatedItemsStep.add (validator.validate (unevaluatedSchema, value, dynamicScope));
                        cntUnevaluatedItems.getAndIncrement ();
                    });
            }

            if (cntUnevaluatedItems.get () > 0)
                unevaluatedItemsAnnotation = true;
        }

        if (prefixItemsAnnotation != null) {
            prefixItemsStep.addAnnotation (prefixItemsAnnotation);
        }

        if (itemsAnnotation != null && itemsAnnotation) {
            itemsStep.addAnnotation (itemsAnnotation);
        }

        if (unevaluatedItemsAnnotation != null)
            unevaluatedItemsStep.addAnnotation(unevaluatedItemsAnnotation);

        if (prefixItemsStep.isNotEmpty ())
            step.add (prefixItemsStep);

        if (itemsStep.isNotEmpty ())
            step.add (itemsStep);

        if (unevaluatedItemsStep.isNotEmpty ())
            step.add (unevaluatedItemsStep);

        return step;
    }

    private @Nullable Integer reducePrefixItemsAnnotations (
        @Nullable Integer currentPrefixItemsAnnotation, Annotations annotations, int instanceSize
    ) {
        Collection<Annotation> otherAnnotations = annotations.getAnnotations ("prefixItems");

        Integer reducedAnnotation = currentPrefixItemsAnnotation;
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

            if (currentPrefixItemsAnnotation != null) {
                if (currentPrefixItemsAnnotation == instanceSize) {
                    booleanItemsAnnotations.add (true);
                } else {
                    integerItemsAnnotations.add (currentPrefixItemsAnnotation);
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

    private @Nullable Boolean reduceItemsAnnotations (
        @Nullable Boolean currentItemsAnnotation, Annotations annotations
    ) {
        Collection<Annotation> otherAnnotations = annotations.getAnnotations ("items");

        Boolean reducedAnnotation = currentItemsAnnotation;
        if (!otherAnnotations.isEmpty ()) {
            List<Boolean> itemsAnnotations = otherAnnotations
                .stream ()
                .filter (a -> a.is (Boolean.class))
                .map (Annotation::asBoolean)
                .collect (Collectors.toList ());

            if (currentItemsAnnotation != null) {
                itemsAnnotations.add (currentItemsAnnotation);
            }

            reducedAnnotation = itemsAnnotations
                .stream ()
                .reduce (false, (current, annotation) -> current |= annotation);
        }

        return reducedAnnotation;
    }

    private @Nullable Boolean reduceUnevaluatedItemsAnnotations (
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

    private Collection<Integer> getContainsAnnotation (Annotations annotations) {
        return annotations.getAnnotations ("contains")
            .stream ()
            .map (Annotation::asIntegers)
            .flatMap (Collection::stream)
            .collect (Collectors.toSet ());
    }
}
