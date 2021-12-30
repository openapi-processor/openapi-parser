/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.memory

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URI

class UriSpec: StringSpec({

    // the slash is required, otherwise the uri is "opaque" and relative resolving does not work.
    "resolve relative memory uri" {
        URI("memory:/foo").resolve("bar").toString() shouldBe "memory:/bar"
    }

})
