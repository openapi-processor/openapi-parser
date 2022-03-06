/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support;

import java.math.BigDecimal;
import java.util.*;

public class Equals {

    @SuppressWarnings ("unchecked")
    public static boolean equals (Object left, Object right) {
        if (left == null && right == null) {
            return true;

        } else if (left instanceof Boolean && right instanceof Boolean) {
            return equalsBoolean ((Boolean)left, (Boolean)right);

        } else if (left instanceof String && right instanceof String) {
            return equalsString ((String)left, (String)right);

        } else if (left instanceof Number && right instanceof Number) {
            return equalsNumber ((Number)left, (Number)right);

        } else if (left instanceof Collection && right instanceof Collection) {
            return equalsArray((Collection<Object>)left, (Collection<Object>)right);

        } else if (left instanceof Map && right instanceof Map) {
            return equalsObject ((Map<String, Object>)left, (Map<String,Object>)right);

        } else {
            return false;
        }
    }

    private static boolean equalsBoolean (Boolean left, Boolean right) {
        return Objects.equals (left, right);
    }

    private static boolean equalsString (String left, String right) {
        return Objects.equals (left, right);
    }

    private static boolean equalsNumber (Number left, Number right) {
        // handles cases like 1 == 1.0
        BigDecimal l = new BigDecimal (left.toString ());
        BigDecimal r = new BigDecimal (right.toString ());
        return l.compareTo (r) == 0;
    }

    private static boolean equalsArray (Collection<Object> left, Collection<Object> right) {
        if (left.size () != right.size ())
            return false;

        Iterator<Object> r = left.iterator ();
        for (Object l : left) {
            if (! equals (l, r.next ())) {
                return false;
            }
        }

        return true;
    }

    private static boolean equalsObject (Map<String, Object> left, Map<String, Object> right) {
        if (left.size () != right.size ())
            return false;

        for (String key : left.keySet ()) {
            if (!right.containsKey (key))
                return false;

            Object lv = left.get (key);
            Object rv = right.get (key);

            if (!equals (lv, rv))
                return false;
        }

        return true;
    }

}
