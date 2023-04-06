/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.*;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * validates type.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.25">
 *     type</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.2">
 *     type</a>
 */
public class Type {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        final Collection<String> types = schema.getType ();
        if (types.isEmpty ())
            return new NullStep ("type");

        TypeStep step = new TypeStep (schema, instance);

        boolean matches = false;
        for (String type : types) {
            if ("null".equals (type) && instance.isNull ()) {
                matches = true;

            } else if ("array".equals (type) && isArray (instance))
                matches = true;

            else if ("boolean".equals (type) && isBoolean (instance))
                matches = true;

            else if ("integer".equals (type) && isInteger (schema, instance))
                matches = true;

            else if ("number".equals (type) && isNumber (instance))
                matches = true;

            else if ("object".equals (type) && isObject (instance))
                matches = true;

            else if ("string".equals (type) && isString (instance))
                matches = true;
        }

        if (!matches) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean isBoolean (JsonInstance instance) {
        return instance.getRawValue () instanceof Boolean;
    }

    private boolean isInteger (JsonSchema schema, JsonInstance instance) {
        Object value = instance.getRawValue ();

        boolean isInteger =
               value instanceof Integer
            || value instanceof Long
            || value instanceof Short
            || value instanceof BigInteger;

        if (isInteger || isDraft4 (schema))
            return isInteger;

        if (value == null || ! isNumber (instance))
            return false;

        return hasZeroFraction (value.toString ());
    }

    private boolean hasZeroFraction (String value) {
        // (value % 1) == 0
        return new BigDecimal(value)
            .remainder (BigDecimal.ONE)
            .compareTo (BigDecimal.ZERO) == 0;
    }

    private boolean isNumber (JsonInstance instance) {
        return instance.getRawValue () instanceof Number;
//            isInteger (instance)
//            || instance instanceof Float
//            || instance instanceof Double
//            || instance instanceof BigDecimal;
    }

    private boolean isString (JsonInstance instance) {
        return instance.getRawValue () instanceof String;
//            || instance instanceof Character;
    }

    private boolean isObject (JsonInstance instance) {
        return instance.getRawValue () instanceof Map;
    }

    private boolean isArray (JsonInstance instance) {
        return instance.getRawValue () instanceof Collection;
    }

    private boolean isDraft4(JsonSchema schema) {
        return SchemaVersion.Draft4.equals (schema.getContext ().getVersion ());
    }
}
