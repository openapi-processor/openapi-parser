/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support;

import java.lang.annotation.*;

/**
 * Marks an experimental api (it has to show that it works in real life). Use at your own risk.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Experimental {}
