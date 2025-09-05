/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.operation as operation30
import io.openapiparser.model.v31.operation as operation31
import io.openapiparser.model.v31.operation as operation32

class OperationSpec: StringSpec({

    "gets operation tags" {
        operation30("tags: [foo, bar]").tags.shouldContainExactly("foo", "bar")
        operation31("tags: [foo, bar]").tags.shouldContainExactly("foo", "bar")
        operation32("tags: [foo, bar]").tags.shouldContainExactly("foo", "bar")
    }

    "gets operation tags is empty if missing" {
        operation30().tags.isEmpty()
        operation31().tags.isEmpty()
        operation32().tags.isEmpty()
    }

    "gets operation summary" {
        operation30("summary: a summary").summary shouldBe "a summary"
        operation31("summary: a summary").summary shouldBe "a summary"
        operation32("summary: a summary").summary shouldBe "a summary"
    }

    "gets operation summary is null if missing" {
        operation30().summary.shouldBeNull()
        operation31().summary.shouldBeNull()
        operation32().summary.shouldBeNull()
    }

    include(testDescription("operation 30", ::operation30) { it.description })
    include(testDescription("operation 31", ::operation31) { it.description })
    include(testDescription("operation 32", ::operation32) { it.description })

    "gets operation external docs object" {
        operation30("externalDocs: {}").externalDocs.shouldNotBeNull()
        operation31("externalDocs: {}").externalDocs.shouldNotBeNull()
        operation32("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets operation external docs object is null if it missing" {
        operation30().externalDocs.shouldBeNull()
        operation31().externalDocs.shouldBeNull()
        operation32().externalDocs.shouldBeNull()
    }

    "gets operation operationId" {
        operation30("operationId: anId").operationId shouldBe "anId"
        operation31("operationId: anId").operationId shouldBe "anId"
        operation32("operationId: anId").operationId shouldBe "anId"
    }

    "gets operation operationId is null if missing" {
        operation30().operationId.shouldBeNull()
        operation31().operationId.shouldBeNull()
        operation32().operationId.shouldBeNull()
    }

    "gets operation parameters" {
        val source = """parameters: [{}, {}]"""

        operation30(source).parameters.size shouldBe 2
        operation31(source).parameters.size shouldBe 2
        operation32(source).parameters.size shouldBe 2
    }

    "gets operation parameters is empty if missing" {
        operation30().parameters.shouldBeEmpty()
        operation31().parameters.shouldBeEmpty()
        operation32().parameters.shouldBeEmpty()
    }

    "gets operation requestBody" {
        operation30("requestBody: {}").requestBody.shouldNotBeNull()
        operation31("requestBody: {}").requestBody.shouldNotBeNull()
        operation32("requestBody: {}").requestBody.shouldNotBeNull()
    }

    "gets operation requestBody is null if it missing" {
        operation30().requestBody.shouldBeNull()
        operation31().requestBody.shouldBeNull()
        operation32().requestBody.shouldBeNull()
    }

    "gets operation callbacks" {
        val source = """
          callbacks:
            foo: {}
            bar: {}
        """

        val c30 = operation30(source).callbacks
        c30.size shouldBe 2
        c30["foo"].shouldNotBeNull()
        c30["bar"].shouldNotBeNull()

        val c31 = operation31(source).callbacks
        c31.size shouldBe 2
        c31["foo"].shouldNotBeNull()
        c31["bar"].shouldNotBeNull()

        val c32 = operation32(source).callbacks
        c32.size shouldBe 2
        c32["foo"].shouldNotBeNull()
        c32["bar"].shouldNotBeNull()
    }

    "gets operation callbacks is empty if missing" {
        operation30().callbacks.shouldBeEmpty()
        operation31().callbacks.shouldBeEmpty()
        operation32().callbacks.shouldBeEmpty()
    }

    "gets operation deprecated" {
        operation30("deprecated: true").deprecated.shouldBeTrue()
        operation30("deprecated: false").deprecated.shouldBeFalse()
        operation31("deprecated: true").deprecated.shouldBeTrue()
        operation31("deprecated: false").deprecated.shouldBeFalse()
        operation32("deprecated: true").deprecated.shouldBeTrue()
        operation32("deprecated: false").deprecated.shouldBeFalse()
    }

    "gets operation deprecated is false if missing" {
        operation30().deprecated.shouldBeFalse()
        operation31().deprecated.shouldBeFalse()
        operation32().deprecated.shouldBeFalse()
    }

    "gets operation security requirements" {
        val s30 = operation30("security: [{}, {}]").security
        s30.shouldNotBeNull()
        s30.size shouldBe 2

        val s31 = operation31("security: [{}, {}]").security
        s31.shouldNotBeNull()
        s31.size shouldBe 2

        val s32 = operation31("security: [{}, {}]").security
        s32.shouldNotBeNull()
        s32.size shouldBe 2
    }

    "gets operation empty security requirements if it is missing" {
        operation30().security.shouldBeEmpty()
        operation31().security.shouldBeEmpty()
        operation32().security.shouldBeEmpty()
    }

    "gets server objects" {
        val s30 = operation30("servers: [{}, {}]").servers
        s30.shouldNotBeNull()
        s30.size shouldBe 2

        val s31 = operation31("servers: [{}, {}]").servers
        s31.shouldNotBeNull()
        s31.size shouldBe 2

        val s32 = operation31("servers: [{}, {}]").servers
        s32.shouldNotBeNull()
        s32.size shouldBe 2
    }

    "gets empty server objects if it is missing" {
        operation30().servers.shouldBeEmpty()
        operation31().servers.shouldBeEmpty()
        operation32().servers.shouldBeEmpty()
    }
})
