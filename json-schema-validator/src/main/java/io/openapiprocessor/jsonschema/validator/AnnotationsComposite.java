/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class AnnotationsComposite implements Annotations {
    private Collection<Annotations> annotations = new ArrayList<> ();

    public void add (Annotations annotations) {
        this.annotations.add (annotations);
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return annotations.stream ()
            .map (a -> a.getAnnotations (keyword))
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }
}
