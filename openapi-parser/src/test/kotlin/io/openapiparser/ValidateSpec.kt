/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.DocumentStore
import io.openapiparser.schema.Resolver
import io.openapiparser.schema.SchemaStore
import io.openapiparser.snakeyaml.SnakeYamlConverter
import io.openapiparser.validator.*
import io.openapiparser.validator.result.FullResultTextBuilder
import io.openapiparser.validator.result.ListResultBuilder

class ValidateSpec: StringSpec({

    val resolver = Resolver(UriReader(), SnakeYamlConverter(), DocumentStore())
    val store = SchemaStore(resolver)

    fun draft4() {
        store.loadDraft4()
    }

    "validate openapi with \$ref into another file" {
        draft4()

        val parser = OpenApiParser(resolver)
        val result = parser.parse("/v30/ref-into-another-file/openapi.yaml")

        val validator = Validator()
        val valid = result.validate(validator, store)

//        val printer = ListResultBuilder(FullResultTextBuilder())
//        val messages = printer.print(result.validationMessages)
//        print(messages)

        valid.shouldBeTrue()
    }

})
