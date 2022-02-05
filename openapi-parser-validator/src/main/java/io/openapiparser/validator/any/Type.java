/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.math.BigInteger;
import java.net.URI;
import java.util.*;

/**
 * validates type.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.2">
 *     Draft 4: type
 * </a>
 */
public class Type {
    private final URI uri;
    private final JsonSchema schema;

    public Type (URI uri, JsonSchema schema) {
        this.uri = uri;
        this.schema = schema;
    }

    public Collection<ValidationMessage> validate (JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        final Collection<String> types = schema.getType ();
        if (types.isEmpty ())
            return messages;

        boolean matches = false;
        for (String type : types) {
            if ("null".equals (type) && instance.isNull ()) {
                matches = true;

            } else if ("array".equals (type) && isArray (instance))
                matches = true;

            else if ("boolean".equals (type) && isBoolean (instance))
                matches = true;

            else if ("integer".equals (type) && isInteger (instance))
                matches = true;

            else if ("number".equals (type) && isNumber (instance))
                matches = true;

            else if ("object".equals (type) && isObject (instance))
                matches = true;

            else if ("string".equals (type) && isString (instance))
                matches = true;
        }

        if (!matches) {
            messages.add (new TypeError (uri.toString (), types));
        }

        return messages;
    }

    private boolean isBoolean (JsonInstance instance) {
        return instance.getRawValue () instanceof Boolean;
    }

    private boolean isInteger (JsonInstance instance) {
        Object value = instance.getRawValue ();
        return value instanceof Integer
            || value instanceof Long
            || value instanceof Short
            || value instanceof BigInteger;
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
}
