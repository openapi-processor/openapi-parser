/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.openapiparser.schema.Bucket

class PropertiesSpec: StringSpec({

    // raw value

    "get raw value is null if missing" {
        Properties(mockk(), Bucket.empty()).getRawValue("missing").shouldBeNull()
    }

    "get raw value" {
        val bucket = Bucket(linkedMapOf<String, Any>("foo" to "bar"))
        Properties(mockk(), bucket).getRawValue("foo").shouldBe("bar")
    }

})
