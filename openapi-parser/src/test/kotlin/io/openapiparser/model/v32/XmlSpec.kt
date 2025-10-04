/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v32.xml as xml32

class XmlSpec: StringSpec({

    "gets xml node type" {
        xml32("nodeType: the node type").nodeType shouldBe "the node type"
    }

    "gets xml node type is null if missing" {
        xml32().nodeType.shouldBeNull()
    }
})
