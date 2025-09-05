/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.model.v30.Example as Example30
import io.openapiparser.model.v30.MediaType as MediaType30
import io.openapiparser.model.v30.header as header30
import io.openapiparser.model.v31.Example as Example31
import io.openapiparser.model.v31.MediaType as MediaType31
import io.openapiparser.model.v31.header as header31
import io.openapiparser.model.v32.Example as Example32
import io.openapiparser.model.v32.MediaType as MediaType32
import io.openapiparser.model.v32.header as header32

class HeaderSpec: StringSpec({

    include(testDescription("header 30", ::header30) { it.description })
    include(testDescription("header 31", ::header31) { it.description })
    include(testDescription("header 32", ::header32) { it.description })

    "gets header required" {
        header30("required: true").required.shouldBeTrue()
        header30("required: false").required.shouldBeFalse()
        header31("required: true").required.shouldBeTrue()
        header31("required: false").required.shouldBeFalse()
        header32("required: true").required.shouldBeTrue()
        header32("required: false").required.shouldBeFalse()
    }

    "gets header required is false if missing" {
        header30().required.shouldBeFalse()
        header31().required.shouldBeFalse()
        header32().required.shouldBeFalse()
    }

    "gets header deprecated" {
        header30("deprecated: true").deprecated.shouldBeTrue()
        header30("deprecated: false").deprecated.shouldBeFalse()
        header31("deprecated: true").deprecated.shouldBeTrue()
        header31("deprecated: false").deprecated.shouldBeFalse()
        header32("deprecated: true").deprecated.shouldBeTrue()
        header32("deprecated: false").deprecated.shouldBeFalse()
    }

    "gets header deprecated is false if missing" {
        header30().deprecated.shouldBeFalse()
        header31().deprecated.shouldBeFalse()
        header32().deprecated.shouldBeFalse()
    }

    "gets header allowEmptyValue" {
        header30("allowEmptyValue: true").allowEmptyValue.shouldBeTrue()
        header30("allowEmptyValue: false").allowEmptyValue.shouldBeFalse()
        header31("allowEmptyValue: true").allowEmptyValue.shouldBeTrue()
        header31("allowEmptyValue: false").allowEmptyValue.shouldBeFalse()
        header32("allowEmptyValue: true").allowEmptyValue.shouldBeTrue()
        header32("allowEmptyValue: false").allowEmptyValue.shouldBeFalse()
    }

    "gets header allowEmptyValue is false if missing" {
        header30().allowEmptyValue.shouldBeFalse()
        header31().allowEmptyValue.shouldBeFalse()
        header32().allowEmptyValue.shouldBeFalse()
    }

    "gets header style" {
        header30("style: simple").style shouldBe "simple"
        header31("style: simple").style shouldBe "simple"
        header32("style: simple").style shouldBe "simple"
    }

    "gets header style is default if missing" {
        header30().style shouldBe "simple"
        header31().style shouldBe "simple"
        header32().style shouldBe "simple"
    }

    "gets header explode" {
        header30("explode: true").explode shouldBe true
        header31("explode: true").explode shouldBe true
        header32("explode: true").explode shouldBe true
    }

    "gets header explode is default if missing" {
        header30("style: simple").explode.shouldBeFalse()
        header31("style: simple").explode.shouldBeFalse()
        header32("style: simple").explode.shouldBeFalse()
    }

    "gets header allowReserved" {
        header30("allowReserved: true").allowReserved.shouldBeTrue()
        header31("allowReserved: true").allowReserved.shouldBeTrue()
        header32("allowReserved: true").allowReserved.shouldBeTrue()
    }

    "gets header allowReserved is false if missing" {
        header30().allowReserved.shouldBeFalse()
        header31().allowReserved.shouldBeFalse()
        header32().allowReserved.shouldBeFalse()
    }

    "gets header schema" {
        header30("schema: {}").schema.shouldNotBeNull()
        header31("schema: {}").schema.shouldNotBeNull()
        header32("schema: {}").schema.shouldNotBeNull()
    }

    "gets header schema is null if missing" {
        header30().schema.shouldBeNull()
        header31().schema.shouldBeNull()
        header32().schema.shouldBeNull()
    }

    "gets header example" {
        header30("example: {}").example.shouldNotBeNull()
        header31("example: {}").example.shouldNotBeNull()
        header32("example: {}").example.shouldNotBeNull()
    }

    "gets header example is null if missing" {
        header30().example.shouldBeNull()
        header31().example.shouldBeNull()
        header32().example.shouldBeNull()
    }

    "gets header examples 30" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = header30(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example30>()
        examples["bar"].shouldBeInstanceOf<Example30>()
    }

    "gets header examples 31" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = header31(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example31>()
        examples["bar"].shouldBeInstanceOf<Example31>()
    }

    "gets header examples 32" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = header32(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example32>()
        examples["bar"].shouldBeInstanceOf<Example32>()
    }

    "gets header examples is empty if missing" {
        header30().examples.shouldBeEmpty()
        header31().examples.shouldBeEmpty()
    }

    "gets header content 30" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = header30(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType30>()
        content["application/xml"].shouldBeInstanceOf<MediaType30>()
    }

    "gets header content 31" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = header31(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType31>()
        content["application/xml"].shouldBeInstanceOf<MediaType31>()
    }

    "gets header content 32" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = header32(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType32>()
        content["application/xml"].shouldBeInstanceOf<MediaType32>()
    }

    "gets header content is empty if missing" {
        header30().content.shouldBeEmpty()
        header31().content.shouldBeEmpty()
        header32().content.shouldBeEmpty()
    }

    include(testExtensions("header30", ::header30) { it.extensions })
    include(testExtensions("header31", ::header31) { it.extensions })
    include(testExtensions("header32", ::header32) { it.extensions })
})
