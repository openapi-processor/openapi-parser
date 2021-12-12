/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.model.v30.parameter as parameter30
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
        parameter30("required: true").required!!.shouldBeTrue()
        parameter30("required: false").required!!.shouldBeFalse()
        parameter31("required: true").required!!.shouldBeTrue()
        parameter31("required: false").required!!.shouldBeFalse()
    }

    "gets parameter required is false if missing" {
        parameter30().required!!.shouldBeFalse()
        parameter31().required!!.shouldBeFalse()
    }

    "gets parameter deprecated" {
        parameter30("deprecated: true").deprecated!!.shouldBeTrue()
        parameter30("deprecated: false").deprecated!!.shouldBeFalse()
        parameter31("deprecated: true").deprecated!!.shouldBeTrue()
        parameter31("deprecated: false").deprecated!!.shouldBeFalse()
    }

    "gets parameter deprecated is false if missing" {
        parameter30().deprecated!!.shouldBeFalse()
        parameter31().deprecated!!.shouldBeFalse()
    }

    "gets parameter allowEmptyValue" {
        parameter30("allowEmptyValue: true").allowEmptyValue!!.shouldBeTrue()
        parameter30("allowEmptyValue: false").allowEmptyValue!!.shouldBeFalse()
        parameter31("allowEmptyValue: true").allowEmptyValue!!.shouldBeTrue()
        parameter31("allowEmptyValue: false").allowEmptyValue!!.shouldBeFalse()
    }

    "gets parameter allowEmptyValue is false if missing" {
        parameter30().allowEmptyValue!!.shouldBeFalse()
        parameter31().allowEmptyValue!!.shouldBeFalse()
    }
})
