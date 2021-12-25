/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.header
import io.openapiparser.model.v30.openapi
import io.openapiparser.model.v31.header
import io.openapiparser.model.v31.openapi
import io.openapiparser.model.v30.operation as operation30
import io.openapiparser.model.v31.operation as operation31

class OperationSpec: StringSpec({

    "gets operation tags" {
        operation30("tags: [foo, bar]").tags.shouldContainExactly("foo", "bar")
        operation31("tags: [foo, bar]").tags.shouldContainExactly("foo", "bar")
    }

    "gets operation tags is empty if missing" {
        operation30().tags.isEmpty()
        operation31().tags.isEmpty()
    }

    "gets operation summary" {
        operation30("summary: a summary").summary shouldBe "a summary"
        operation31("summary: a summary").summary shouldBe "a summary"
    }

    "gets operation summary is null if missing" {
        operation30().summary.shouldBeNull()
        operation31().summary.shouldBeNull()
    }

    include(testDescription("operation 30", ::operation30) { it.description })
    include(testDescription("operation 31", ::operation30) { it.description })

    "gets operation external docs object" {
        operation30("externalDocs: {}").externalDocs.shouldNotBeNull()
        operation31("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets operation external docs object is null if it missing" {
        operation30().externalDocs.shouldBeNull()
        operation31().externalDocs.shouldBeNull()
    }

    "gets operation operationId" {
        operation30("operationId: anId").operationId shouldBe "anId"
        operation31("operationId: anId").operationId shouldBe "anId"
    }

    "gets operation operationId is null if missing" {
        operation30().operationId.shouldBeNull()
        operation31().operationId.shouldBeNull()
    }

    "gets operation parameters" {
        val source = """parameters: [{}, {}]"""

        forAll(
            operation30(source).parameters,
            operation31(source).parameters
        ) { parameters ->
            parameters.size shouldBe 2
        }
    }

    "gets operation parameters is empty if missing" {
        operation30().parameters.shouldBeEmpty()
        operation31().parameters.shouldBeEmpty()
    }

    "gets operation requestBody" {
        operation30("requestBody: {}").requestBody.shouldNotBeNull()
        operation31("requestBody: {}").requestBody.shouldNotBeNull()
    }

    "gets operation requestBody is null if it missing" {
        operation30().requestBody.shouldBeNull()
        operation31().requestBody.shouldBeNull()
    }

    "gets operation callbacks" {
        val source = """
          callbacks:
            foo: {}
            bar: {}
        """

        forAll(
            operation30(source).callbacks,
            operation31(source).callbacks
        ) { callbacks ->
            callbacks.size shouldBe 2
            callbacks["foo"].shouldNotBeNull()
            callbacks["bar"].shouldNotBeNull()
        }
    }

    "gets operation callbacks is empty if missing" {
        operation30().callbacks.shouldBeEmpty()
        operation31().callbacks.shouldBeEmpty()
    }

    "gets operation deprecated" {
        operation30("deprecated: true").deprecated.shouldBeTrue()
        operation30("deprecated: false").deprecated.shouldBeFalse()
        operation31("deprecated: true").deprecated.shouldBeTrue()
        operation31("deprecated: false").deprecated.shouldBeFalse()
    }

    "gets operation deprecated is false if missing" {
        operation30().deprecated.shouldBeFalse()
        operation31().deprecated.shouldBeFalse()
    }

    "gets operation security requirements" {
        forAll(
            operation30("security: [{}, {}]").security,
            operation31("security: [{}, {}]").security
        ) { security ->
            security.shouldNotBeNull()
            security.size shouldBe 2
        }
    }

    "gets operation empty security requirements if it is missing" {
        operation30().security.shouldBeEmpty()
        operation31().security.shouldBeEmpty()
    }

    "gets server objects" {
        forAll(
            operation30("servers: [{}, {}]").servers,
            operation31("servers: [{}, {}]").servers
        ) { servers ->
            servers.shouldNotBeNull()
            servers.size shouldBe 2
        }
    }

    "gets empty server objects if it is missing" {
        operation30().servers.shouldBeEmpty()
        operation31().servers.shouldBeEmpty()
    }
})
