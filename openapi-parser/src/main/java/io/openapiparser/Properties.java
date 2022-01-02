/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.*;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

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
    public boolean hasProperty (String property) {
        return bucket.hasProperty (property);
    }

    /* Strings */

    protected @Nullable String getStringOrNull (String property) {
        return bucket.convert (property, new StringNullableConverter ());
    }

    protected String getStringOrThrow (String property) {
        return bucket.convert (property, new StringNotNullConverter ());
    }

    /* Numbers */

    protected @Nullable Number getNumberOrNull (String property) {
        return bucket.convert (property, new NumberConverter ());
    }

    /* Integers */

    protected @Nullable Integer getIntegerOrNull (String property) {
        return bucket.convert (property, new IntegerConverter ());
    }

    protected @Nullable Integer getIntegerOrDefault (String property, int defaultValue) {
        Integer value = bucket.convert (property, new IntegerConverter ());
        if (value == null)
            return defaultValue;

        return value;
    }

    /* Booleans */

    protected @Nullable Boolean getBooleanOrNull (String property) {
        return bucket.convert (property, new BooleanConverter ());
    }

    protected @Nullable Boolean getBooleanOrFalse (String property) {
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
        return bucket.convert (property, new ObjectNullableConverter<> (context, clazz));
    }

    protected <T> T getObjectOrThrow (String property, Class<T> clazz) {
        return bucket.convert (property, new ObjectNotNullConverter<> (context, clazz));
    }

    /* Collections */

    protected <T> Collection<T> getObjectsOrEmpty (String property, Class<T> clazz) {
        return bucket.convert (property, new ObjectsOrEmptyConverter<> (context, clazz));
    }

    /* String Collections */

    protected Collection<String> getStringsOrEmpty (String property) {
        return bucket.convert (property, new StringsOrEmptyConverter ());
    }

    /* Maps */

    protected Map<String, String> getMapStringsOrEmpty (String property) {
        return getMapObjectsOrEmpty (property, String.class);
    }

    protected <T> Map<String, T> getMapObjectsOrEmpty (Class<T> clazz) {
        return bucket.convert (new MapObjectsOrEmptySelfConverter<> (context, clazz));
    }

    protected <T> Map<String, T> getMapObjectsOrEmpty (String property, Class<T> clazz) {
        return bucket.convert (property, new MapObjectsOrEmptyConverter<> (context, clazz));
    }

    /* other */

    protected Map<String, Object> getExtensions () {
        return bucket.convert (new ExtensionsConverter ());
    }

    /* ref */

    protected <T> T getRefObject (Class<T> clazz) {
        return create (context.getRefObjectOrNull (bucket), clazz);
    }

    protected <T> T getRefObjectOrThrow (Class<T> clazz) {
        return create (context.getRefObjectOrThrow (bucket), clazz);
    }

    private <T> T create (Bucket bucket, Class<T> clazz) {
        try {
            return clazz
                .getDeclaredConstructor (Context.class, Bucket.class)
                .newInstance (context, bucket);
        } catch (Exception e) {
            throw new RuntimeException (String.format("failed to create %s", clazz.getName ()), e);
        }
    }

}
