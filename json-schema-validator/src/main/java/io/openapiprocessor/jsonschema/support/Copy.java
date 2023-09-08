/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support;

import org.checkerframework.checker.nullness.qual.PolyNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

/**
 * "deep" copy an object tree. It only handles the types to represent a json document. Map and Collection are cloned,
 * all other immutable "primitive" types are not cloned.
 */
public class Copy {

    private Copy() {}

    public static @PolyNull Object deep (@PolyNull Object source) {
        if (source == null)
            return null;

        if (source instanceof Map) {
            Map<String, Object> items = Types.asObject (source);
            Map<String, Object> copy = new LinkedHashMap<> ();

            items.forEach ((key, value) -> {
                copy.put (key, deep (value));
            });
            return copy;

        } else if (source instanceof Collection) {
            Collection<Object> items = Types.asArray (source);
            Collection<Object> copy = new LinkedList<> ();

            for (Object item : items) {
                copy.add (deep (item));
            }
            return copy;
        }

        return Function.identity ().apply (source);
    }
}
