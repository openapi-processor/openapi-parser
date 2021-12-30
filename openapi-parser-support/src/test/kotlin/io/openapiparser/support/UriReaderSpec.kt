/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.memory.Memory
import java.net.URI

class UriReaderSpec: StringSpec({

    "reads file by uri" {
        val openapi = "openapi: 3.0.3"
        Memory.add("/openapi.yaml", openapi)

        val reader = UriReader()
        val content = reader.read(URI("memory:/openapi.yaml"))

        Strings.of(content).shouldBe(openapi)
    }

    afterEach {
        Memory.clear()
    }
})


