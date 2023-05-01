/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.schema.UriSupport.*

class UriSupportSpec : StringSpec({

    "strip fragment from opaque uri" {
        val uri = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#/root")
        val expected = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed")
        stripFragment(uri).shouldBe(expected)
    }

    "resolves fragment on opaque uri" {
        val uri = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed")
        val frag = createUri("#/root")
        val expected = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#/root")
        resolve(uri, frag).shouldBe(expected)
    }

    "resolves absolute opaque uri" {
        val uri = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#/root")
        val absolute = createUri("urn:uuid:deadbeef-1234-eeee-eeee-4321feebdaed#/other")
        resolve(uri, absolute).shouldBe(absolute)
    }

    "resolves fragment without slash on opaque uri with fragment" {
        val uri = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#id")
        val absolute = createUri("#newId")
        val expected = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#newId")
        resolve(uri, absolute).shouldBe(expected)
    }

    "resolves fragment with slash on opaque uri with fragment" {
        val uri = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#id")
        val absolute = createUri("#/type")
        val expected = createUri("urn:uuid:deadbeef-1234-ffff-ffff-4321feebdaed#id/type")
        resolve(uri, absolute).shouldBe(expected)
    }
})
