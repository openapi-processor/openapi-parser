/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Collection;

public interface OpenApiResult {

    enum Version { V30, V31 }

    Version getVersion();

    <T> T getModel(Class<T> api);

    Collection<ValidationMessage> getValidationMessages();

}
