/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.interfaces;

import java.io.IOException;

/**
 * write a document. Should handle security if required.
 */
public interface Writer {

    /**
     * write a document.
     *
     * @param document the document
     * @throws IOException if writing fails.
     */

    void write(Object document) throws IOException;
}
