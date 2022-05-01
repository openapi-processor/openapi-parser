/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.result;

import java.util.Objects;

public class MessageKey {
    final String instanceScope;
    final String instancePath;

    public MessageKey (String instanceScope, String instancePath) {
        this.instanceScope = instanceScope;
        this.instancePath = instancePath;
    }

    @Override
    public String toString () {
        return String.format ("%s - %s", instanceScope, instancePath);
    }

    @Override
    public boolean equals (Object o) {
        if (this == o)
            return true;

        if (o == null || getClass () != o.getClass ())
            return false;

        MessageKey that = (MessageKey) o;
        return Objects.equals (instanceScope, that.instanceScope)
            && Objects.equals (instancePath, that.instancePath);
    }

    @Override
    public int hashCode () {
        return Objects.hash (instanceScope, instancePath);
    }
}
