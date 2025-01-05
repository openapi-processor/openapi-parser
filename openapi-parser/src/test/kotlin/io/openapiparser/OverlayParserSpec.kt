/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.OverlayBuilder
import java.net.URI

class OverlayParserSpec: StringSpec ({

    "parses overlay 1.0" {
        val parser = OverlayBuilder()
            .withOverlay("""
                overlay: 1.0.0
            """.trimIndent())
            .buildParser()

        val result = parser.parse(URI("file:///overlay.yaml"))

        result.version shouldBe OverlayResult.Version.V10
    }
})
