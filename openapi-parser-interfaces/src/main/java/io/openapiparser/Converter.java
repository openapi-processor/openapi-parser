/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * yaml/json parser abstraction.
 */
public interface Converter {

    /**
     * converts a source (json/yaml) document into a java object tree. The tree structure is based
     * on {@code Map<String, Object>}. The root may not be {@link java.util.Map} if the document
     * just contains a single value.
     *
     * @param api a json/yaml document.
     * @return object tree of the document.
     */
    Object convert (String api) throws ConverterException;
}
