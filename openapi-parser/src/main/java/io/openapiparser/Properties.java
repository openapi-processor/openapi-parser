/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.*;
import io.openapiparser.schema.Bucket;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.support.Experimental;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * base class of OpenAPI model objects to reduce duplication.
 */
public class Properties {
    protected final Context context;
    protected final Bucket bucket;

    protected Properties (Context context, Bucket bucket) {
        this.context = context;
        this.bucket = bucket;
    }

    /* json pointer */

    @Experimental
    public @Nullable Object getRawValueOf (String pointer) {
        return bucket.getRawValue (JsonPointer.from (pointer));
    }

    @Experimental
    public <T> @Nullable T getValueOf (String pointer, Class<T> target) {
        final Object rawValue = getRawValueOf (pointer);
        if (rawValue == null)
            throw new NoValueException (pointer);

        return new ObjectNotNullConverter<> (bucket.getScope (), new Factory<> (context, target))
            .convert ("unused", rawValue, pointer);
    }

    /* raw */

    protected @Nullable Object getRawValue (String property) {
        return bucket.getRawValue (property);
    }

    /**
     * checks if the properties contains the given property name.
     *
     * @param property property name
     * @return true if the property exists, else false
     */
    public /*protected*/ boolean hasProperty (String property) {
        return bucket.hasProperty (property);
    }

    /* Strings */

    protected @Nullable String getStringOrNull (String property) {
        return bucket.convert (property, new StringNullableConverter ());
    }

    protected String getStringOrThrow (String property) {
        return nonNull (bucket.convert (property, new StringNotNullConverter ()));
    }

    /* Numbers */

    protected @Nullable Number getNumberOrNull (String property) {
        return bucket.convert (property, new NumberConverter ());
    }

    /* Integers */

    protected @Nullable Integer getIntegerOrNull (String property) {
        return bucket.convert (property, new IntegerConverter ());
    }

    protected Integer getIntegerOrDefault (String property, int defaultValue) {
        Integer value = bucket.convert (property, new IntegerConverter ());
        if (value == null)
            return defaultValue;

        return value;
    }

    /* Booleans */

    protected @Nullable Boolean getBooleanOrNull (String property) {
        return bucket.convert (property, new BooleanConverter ());
    }

    protected Boolean getBooleanOrFalse (String property) {
        return getBooleanOrDefault (property, false);
    }

    protected Boolean getBooleanOrDefault (String property, boolean defaultValue) {
        Boolean value = getBooleanOrNull (property);
        if (value == null)
            return defaultValue;

        return value;
    }

    /* Objects */

    protected <T> @Nullable T getObjectOrNull (String property, Class<T> clazz) {
        return getObjectOrNull (bucket, property, clazz);
    }

    protected <T> T getObjectOrThrow (String property, Class<T> clazz) {
        return getObjectOrThrow (bucket, property, clazz);
    }

    /* Collections */

    protected <T> Collection<T> getObjectsOrEmpty (String property, Class<T> clazz) {
        return nonNull (bucket.convert (property, new ObjectsOrEmptyConverter<T> (
            bucket.getScope (), new Factory<> (context, clazz))));
    }

    /* String Collections */

    protected Collection<String> getStringsOrEmpty (String property) {
        return nonNull (bucket.convert (property, new StringsOrEmptyConverter ()));
    }

    protected @Nullable Collection<String> getStringsOrNull (String property) {
        return bucket.convert (property, new StringsNullableConverter ());
    }

    /* Maps */

    protected Map<String, String> getMapStringsOrEmpty (String property) {
        return nonNull (bucket.convert (property, new MapStringsOrEmptyConverter()));
    }

    protected <T> Map<String, T> getMapObjectsOrEmpty (Class<T> clazz) {
        Map<String, T> objects = new LinkedHashMap<> ();

        bucket.forEachProperty (property -> {
            objects.put (property, getObjectOrThrow (bucket, property, clazz));
        });

        return Collections.unmodifiableMap (objects);
    }

    protected <T> Map<String, T> getMapObjectsOrEmpty (String property, Class<T> clazz) {
        return nonNull (bucket.convert (
            property,
            new MapObjectsOrEmptyConverter<T> (bucket.getScope (),
            new Factory<T> (context, clazz))));
    }

    protected Map<String, Set<String>> getMapSetStringsOrEmpty (String property) {
        return nonNull (bucket.convert (property, new MapSetStringsOrEmptyConverter ()));
    }

    /* other */

    protected Map<String, Object> getExtensions () {
        return nonNull (bucket.convert (new ExtensionsConverter ()));
    }

    /* ref */

    // todo getRefObjectOrNull ??
    protected <T> @Nullable T getRefObject (Class<T> clazz) {
        final Bucket ref = context.getRefObjectOrNull (bucket);
        if (ref == null)
            return null;

        return new Factory<T> (context, clazz).create (ref);
    }

    protected <T> T getRefObjectOrThrow (Class<T> clazz) {
        final Bucket refObjectOrThrow = context.getRefObjectOrThrow (bucket);
        return new Factory<T> (context, clazz).create (refObjectOrThrow);
    }

    /* helper */

    private <T> @Nullable T getObjectOrNull (Bucket source, String property, Class<T> clazz) {
        final Bucket value = source.getBucket (property);
        if (value == null)
            return null;

        return new Factory<T> (context, clazz).create (value);
    }

    private <T> T getObjectOrThrow (Bucket source, String property, Class<T> clazz) {
        final Bucket value = source.getBucket (property);
        if (value == null)
            throw new NoValueException (property);

        return new Factory<T> (context, clazz).create (value);
    }
}
