/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator;

import java.util.Collection;
import java.util.Collections;

public interface Annotations {
    default Collection<Annotation> getAnnotations (String keyword) {
        return Collections.emptyList ();
    }
}
