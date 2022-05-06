/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support;

import java.lang.annotation.*;

/**
 * marks experimental apis that should not (yet..) be used in production code. It is used for
 * documentation only.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Experimental {}
