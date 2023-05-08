/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiparser.model.v30.Example as Example30
import io.openapiparser.model.v30.MediaType as MediaType30
import io.openapiparser.model.v30.parameter as parameter30
import io.openapiparser.model.v31.Example as Example31
import io.openapiparser.model.v31.MediaType as MediaType31
import io.openapiparser.model.v31.parameter as parameter31

class ParameterSpec: StringSpec({

    "gets parameter name" {
        parameter30("name: aName").name shouldBe "aName"
        parameter31("name: aName").name shouldBe "aName"
    }

    "gets parameter name throws if it is missing" {
        shouldThrow<NoValueException> { parameter30().name }
        shouldThrow<NoValueException> { parameter31().name }
    }

    "gets parameter in" {
        parameter30("in: inType").`in` shouldBe "inType"
        parameter31("in: inType").`in` shouldBe "inType"
    }

    "gets parameter in throws if it is missing" {
        shouldThrow<NoValueException> { parameter30().`in` }
        shouldThrow<NoValueException> { parameter31().`in` }
    }

    include(testDescription("parameter 30", ::parameter30) { it.description })
    include(testDescription("parameter 31", ::parameter31) { it.description })

    "gets parameter required" {
        parameter30("required: true").required.shouldBeTrue()
        parameter30("required: false").required.shouldBeFalse()
        parameter31("required: true").required.shouldBeTrue()
        parameter31("required: false").required.shouldBeFalse()
    }

    "gets parameter required is false if missing" {
        parameter30().required.shouldBeFalse()
        parameter31().required.shouldBeFalse()
    }

    "gets parameter deprecated" {
        parameter30("deprecated: true").deprecated.shouldBeTrue()
        parameter30("deprecated: false").deprecated.shouldBeFalse()
        parameter31("deprecated: true").deprecated.shouldBeTrue()
        parameter31("deprecated: false").deprecated.shouldBeFalse()
    }

    "gets parameter deprecated is false if missing" {
        parameter30().deprecated.shouldBeFalse()
        parameter31().deprecated.shouldBeFalse()
    }

    "gets parameter allowEmptyValue" {
        parameter30("allowEmptyValue: true").allowEmptyValue.shouldBeTrue()
        parameter30("allowEmptyValue: false").allowEmptyValue.shouldBeFalse()
        parameter31("allowEmptyValue: true").allowEmptyValue.shouldBeTrue()
        parameter31("allowEmptyValue: false").allowEmptyValue.shouldBeFalse()
    }

    "gets parameter allowEmptyValue is false if missing" {
        parameter30().allowEmptyValue.shouldBeFalse()
        parameter31().allowEmptyValue.shouldBeFalse()
    }

    "gets parameter style" {
        parameter30("style: a Style").style shouldBe "a Style"
        parameter31("style: a Style").style shouldBe "a Style"
    }

    "gets parameter style is default if missing" {
        parameter30("in: query").style shouldBe "form"
        parameter30("in: cookie").style shouldBe "form"
        parameter30("in: path").style shouldBe "simple"
        parameter30("in: header").style shouldBe "simple"
        parameter31("in: query").style shouldBe "form"
        parameter31("in: cookie").style shouldBe "form"
        parameter31("in: path").style shouldBe "simple"
        parameter31("in: header").style shouldBe "simple"
    }

    "gets parameter explode" {
        parameter30("explode: true").explode shouldBe true
        parameter31("explode: true").explode shouldBe true
    }

    "gets parameter explode is default if missing" {
        parameter30("style: form").explode.shouldBeTrue()
        parameter30("style: simple").explode.shouldBeFalse()
        parameter31("style: form").explode.shouldBeTrue()
        parameter31("style: simple").explode.shouldBeFalse()
    }

    "gets parameter allowReserved" {
        parameter30("allowReserved: true").allowReserved.shouldBeTrue()
        parameter31("allowReserved: true").allowReserved.shouldBeTrue()
    }

    "gets parameter allowReserved is false if missing" {
        parameter30().allowReserved.shouldBeFalse()
        parameter31().allowReserved.shouldBeFalse()
    }

    "gets parameter schema" {
        parameter30("schema: {}").schema.shouldNotBeNull()
        parameter31("schema: {}").schema.shouldNotBeNull()
    }

    "gets parameter schema is null if missing" {
        parameter30().schema.shouldBeNull()
        parameter31().schema.shouldBeNull()
    }

    "gets parameter example" {
        parameter30("example: {}").example.shouldNotBeNull()
        parameter31("example: {}").example.shouldNotBeNull()
    }

    "gets parameter example is null if missing" {
        parameter30().example.shouldBeNull()
        parameter31().example.shouldBeNull()
    }

    "gets parameter examples 30" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = parameter30(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example30>()
        examples["bar"].shouldBeInstanceOf<Example30>()
    }

    "gets parameter examples 31" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = parameter31(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example31>()
        examples["bar"].shouldBeInstanceOf<Example31>()
    }

    "gets parameter examples is empty if missing" {
        parameter30().examples.shouldBeEmpty()
        parameter31().examples.shouldBeEmpty()
    }

    "gets parameter content 30" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = parameter30(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType30>()
        content["application/xml"].shouldBeInstanceOf<MediaType30>()
    }

    "gets parameter content 31" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = parameter31(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType31>()
        content["application/xml"].shouldBeInstanceOf<MediaType31>()
    }

    "gets parameter content is empty if missing" {
        parameter30().content.shouldBeEmpty()
        parameter31().content.shouldBeEmpty()
    }

    include(testExtensions("parameter30", ::parameter30) { it.extensions })
    include(testExtensions("parameter31", ::parameter31) { it.extensions })
})
