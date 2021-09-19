package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class OpenApiResultSpec: StringSpec({

    "should have version 30" {
        val ctx = mockk<Context>()
        val result = OpenApiResult30(ctx)

        result.version shouldBe OpenApiResult.Version.V30
    }

    "should return api 30" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns emptyMap()

        val result = OpenApiResult30(ctx)

        result.getModel(OpenApi30::class.java).shouldBeInstanceOf<OpenApi30>()
    }

    "should throw if model type does not match api version 30" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns emptyMap()

        val result = OpenApiResult30(ctx)

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi31::class.java)
        }
    }

    "should have version 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns emptyMap()

        val result = OpenApiResult31(ctx)

        result.version shouldBe OpenApiResult.Version.V31
    }

    "should throw if model type does not match api version 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns emptyMap()

        val result = OpenApiResult31(ctx)

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi30::class.java)
        }
    }

    "should return api 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns emptyMap()

        val result = OpenApiResult31(ctx)

        result.getModel(OpenApi31::class.java).shouldBeInstanceOf<OpenApi31>()
    }

})
