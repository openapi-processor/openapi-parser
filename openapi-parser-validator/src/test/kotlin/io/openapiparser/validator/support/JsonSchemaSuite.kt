/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

/**
 * test suite mapping of [JSON-Schema-Test-Suite](https://github.com/json-schema-org/JSON-Schema-Test-Suite)
 */

data class Suite(
    val description: String,
    val schema: Any,
    val tests: List<Test>
)

data class Test(
    val description: String,
    val data: Any,
    val valid: Boolean
)
