/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.model.v31.OAuthFlows as Flows31
import io.openapiparser.model.v32.OAuthFlows as Flows32
import io.openapiparser.model.v31.securityScheme as securityScheme31
import io.openapiparser.model.v32.securityScheme as securityScheme32
import io.openapiprocessor.jsonschema.converter.NoValueException

class SecuritySchemeSpec: StringSpec({

    "gets security scheme type" {
        securityScheme31("type: the type").type shouldBe "the type"
        securityScheme32("type: the type").type shouldBe "the type"
    }

    "gets security scheme type throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().type }
        shouldThrow<NoValueException> { securityScheme32().type }
    }

    "gets security scheme description" {
        securityScheme31("description: a description").description shouldBe "a description"
        securityScheme32("description: a description").description shouldBe "a description"
    }

    "gets security scheme description is null if missing" {
        securityScheme31().description.shouldBeNull()
        securityScheme32().description.shouldBeNull()
    }

    "gets security scheme name" {
        securityScheme31("name: the name").name shouldBe "the name"
        securityScheme32("name: the name").name shouldBe "the name"
    }

    "gets security scheme name throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().name }
        shouldThrow<NoValueException> { securityScheme32().name }
    }

    "gets security scheme in" {
        securityScheme31("in: a in").`in` shouldBe "a in"
        securityScheme32("in: a in").`in` shouldBe "a in"
    }

    "gets security scheme in throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().`in` }
        shouldThrow<NoValueException> { securityScheme32().`in` }
    }

    "gets security scheme scheme" {
        securityScheme31("scheme: the scheme").scheme shouldBe "the scheme"
        securityScheme32("scheme: the scheme").scheme shouldBe "the scheme"
    }

    "gets security scheme scheme throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().scheme }
        shouldThrow<NoValueException> { securityScheme32().scheme }
    }

    "gets security scheme bearer format" {
        securityScheme31("bearerFormat: a bearer format").bearerFormat shouldBe "a bearer format"
        securityScheme32("bearerFormat: a bearer format").bearerFormat shouldBe "a bearer format"
    }

    "gets security scheme bearer format is null if missing" {
        securityScheme31().bearerFormat.shouldBeNull()
        securityScheme32().bearerFormat.shouldBeNull()
    }

    "gets security scheme flows" {
        securityScheme31("flows: {}").flows.shouldBeInstanceOf<Flows31>()
        securityScheme32("flows: {}").flows.shouldBeInstanceOf<Flows32>()
    }

    "gets security scheme flows throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().flows }
        shouldThrow<NoValueException> { securityScheme32().flows }
    }

    "gets security scheme OPENID connect url" {
        securityScheme31("openIdConnectUrl: the url").openIdConnectUrl shouldBe "the url"
        securityScheme32("openIdConnectUrl: the url").openIdConnectUrl shouldBe "the url"
    }

    "gets security scheme OPENID connect url throws if missing" {
        shouldThrow<NoValueException> { securityScheme31().openIdConnectUrl }
        shouldThrow<NoValueException> { securityScheme32().openIdConnectUrl }
    }
})
