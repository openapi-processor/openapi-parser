/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.SchemaVersion;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * validates type.
 * <p>Specification: since Draft 4
 */
public class Type {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        final Collection<String> types = schema.getType ();
        if (types.isEmpty ())
            return;

        TypeStep step = new TypeStep (schema, instance);

        boolean matches = false;
        for (String type : types) {
            matches |= matches(type, schema, instance);
        }

        if (!matches) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean matches(String type, JsonSchema schema, JsonInstance instance) {
        if ("null".equals (type) && instance.isNull ()) {
            return true;

        } else if ("array".equals (type) && isArray (instance))
            return true;

        else if ("boolean".equals (type) && isBoolean (instance))
            return true;

        else if ("integer".equals (type) && isInteger (schema, instance))
            return true;

        else if ("number".equals (type) && isNumber (instance))
            return true;

        else if ("object".equals (type) && isObject (instance))
            return true;

        else return "string".equals(type) && isString(instance);
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
