/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support;

public class ValidInt {
    private final boolean valid;
    private final int value;

    public ValidInt () {
        this.valid = false;
        this.value = 0;
    }

    public ValidInt (int value) {
        this.valid = true;
        this.value = value;
    }

    public boolean isValid () {
        return valid;
    }

    public int getValue () {
        return value;
    }

    public boolean isInRange (int min, int max) {
        if (!valid)
            return false;

        return value >= min && value <= max;
    }

    public static ValidInt parse (String source) {
        try {
            return new ValidInt (Integer.parseInt (source));
        } catch (Exception ex) {
            return new ValidInt ();
        }
    }
}
