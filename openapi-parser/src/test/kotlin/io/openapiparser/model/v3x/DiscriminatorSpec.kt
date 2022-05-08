/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.NoValueException
import io.openapiparser.model.v30.discriminator as discriminator30
import io.openapiparser.model.v31.discriminator as discriminator31

class DiscriminatorSpec : StringSpec({

    "gets discriminator propertyName" {
        discriminator30("propertyName: foo").propertyName shouldBe "foo"
        discriminator31("propertyName: foo").propertyName shouldBe "foo"
    }

    "gets discriminator propertyName throws if it is missing" {
        shouldThrow<NoValueException> { discriminator30().propertyName }
        shouldThrow<NoValueException> { discriminator31().propertyName }
    }

    "gets discriminator mapping" {
        val source = """
           mapping:
             foo: '#/components/Foo'
        """

        val d30 = discriminator30(source).mapping
        d30.shouldNotBeNull()
        d30.size shouldBe 1

        val d31 = discriminator31(source).mapping
        d31.shouldNotBeNull()
        d31.size shouldBe 1
    }

    "gets discriminator mapping is empty if it is missing" {
        discriminator30().mapping.shouldBeEmpty()
        discriminator31().mapping.shouldBeEmpty()
    }
})
