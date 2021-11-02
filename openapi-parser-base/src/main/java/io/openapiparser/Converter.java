/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Map;

/**
 * yaml/json parser abstraction.
 */
public interface Converter {

    /**
     * converts an OpenAPI description in json and/or yaml format into a map tree.
     *
     * @param api an OpenAPI description in json or yaml format.
     * @return map tree of the OpenAPI description.
     */
    Map<String, Object> convert(String api) throws ConverterException;

}
