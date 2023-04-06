/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support;

import org.checkerframework.checker.nullness.qual.PolyNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

import static io.openapiparser.converter.Types.asArray;
import static io.openapiparser.converter.Types.asObject;

/**
 * "deep" copy an object tree. It only handles the types to represent a json document. Map & Collection are cloned,
 * all other immutable "primitive" types are not cloned.
 */
public class Copy {

    public static @PolyNull Object deep (@PolyNull Object source) {
        if (source == null)
            return null;

        if (source instanceof Map) {
            Map<String, Object> items = asObject (source);
            Map<String, Object> copy = new LinkedHashMap<> ();

            items.forEach ((key, value) -> {
                copy.put (key, deep (value));
            });
            return copy;

        } else if (source instanceof Collection) {
            Collection<Object> items = asArray (source);
            Collection<Object> copy = new LinkedList<> ();

            for (Object item : items) {
                copy.add (deep (item));
            }
            return copy;
        }

        return Function.identity ().apply (source);
    }
}
