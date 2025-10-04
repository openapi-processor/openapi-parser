/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ServerSpec: StringSpec({

    "gets server name" {
        server("name: server name").name shouldBe "server name"
    }

    "gets server name is null if missing" {
        server().name.shouldBeNull()
    }
})
