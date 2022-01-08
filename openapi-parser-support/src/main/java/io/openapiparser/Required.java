/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.lang.annotation.*;

/**
 * marks required OpenAPI properties. It is used for documentation only.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Required {}
