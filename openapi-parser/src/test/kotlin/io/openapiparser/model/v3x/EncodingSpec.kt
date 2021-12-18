/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.encoding as encoding30
import io.openapiparser.model.v31.encoding as encoding31

class EncodingSpec: StringSpec({

    "gets encoding content type" {
        encoding30("contentType: application/json").contentType shouldBe "application/json"
        encoding31("contentType: application/json").contentType shouldBe "application/json"
    }

    /* todo default values
    "gets encoding content type default if missing" {
        val source = """
            schema:
              type: object
              properties:
                foo:
                  type: string
                binary:
                  type: string
                  format: binary
                object:
                  type: object
                  properties:
                    any:
                      type: string
                array:
                  type: array
                  items:
                    type: string

        """.trimIndent()

        mediaType30(source).encoding["foo"]?.contentType shouldBe "plain/text"
        mediaType30(source).encoding["binary"]?.contentType shouldBe "application/octet-stream"
        mediaType30(source).encoding["object"]?.contentType shouldBe "application/json"
        mediaType30(source).encoding["array"]?.contentType shouldBe "plain/text"
    }*/

    "gets encoding headers" {
        encoding30("headers: { X-Foo: {}}").headers["X-Foo"].shouldNotBeNull()
        encoding31("headers: { X-Foo: {}}").headers["X-Foo"].shouldNotBeNull()
    }

    "gets encoding headers is empty if missing" {
        encoding30().headers.isEmpty()
        encoding31().headers.isEmpty()
    }

    // not sure if style is correct ...
    "gets encoding style" {
        encoding30("style: form").style shouldBe "form"
        encoding31("style: form").style shouldBe "form"
    }

    "gets encoding style default if missing" {
        encoding30().style shouldBe "form"
        encoding31().style shouldBe "form"
    }

    "gets parameter explode" {
        encoding30("explode: true").explode shouldBe true
        encoding31("explode: true").explode shouldBe true
    }

    "gets parameter explode is default if missing" {
        encoding30("style: form").explode.shouldBeTrue()
        encoding30("style: simple").explode.shouldBeFalse()
        encoding31("style: form").explode.shouldBeTrue()
        encoding31("style: simple").explode.shouldBeFalse()
    }

    "gets parameter allowReserved" {
        encoding30("allowReserved: true").allowReserved.shouldBeTrue()
        encoding31("allowReserved: true").allowReserved.shouldBeTrue()
    }

    "gets parameter allowReserved is false if missing" {
        encoding30().allowReserved.shouldBeFalse()
        encoding31().allowReserved.shouldBeFalse()
    }

    include(testExtensions("encoding 30", ::encoding30) { it.extensions })
    include(testExtensions("encoding 31", ::encoding31) { it.extensions })
})

