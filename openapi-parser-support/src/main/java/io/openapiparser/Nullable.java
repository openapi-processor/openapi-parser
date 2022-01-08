/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.lang.annotation.*;

/**
 * marks nullable objects.
 *
 * This is not used because checkerframework does not recognize @Nullable. It matches the full
 * package name.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.PARAMETER})
public @interface Nullable {}
