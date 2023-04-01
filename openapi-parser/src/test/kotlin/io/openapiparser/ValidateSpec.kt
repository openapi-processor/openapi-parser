/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.*
import io.openapiparser.snakeyaml.SnakeYamlConverter
import io.openapiparser.validator.*
import io.openapiparser.validator.result.FullResultTextBuilder
import io.openapiparser.validator.result.ListResultBuilder
import io.openapiparser.validator.result.MessageCollector
import io.openapiparser.validator.result.MessageTextBuilder

class ValidateSpec: StringSpec({

    "validate openapi with \$ref into another file" {
        val loader = DocumentLoader(UriReader(), SnakeYamlConverter())

        val documents = DocumentStore()
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver (documents, loader, settings)

        val store = SchemaStore(loader)
        store.registerDraft4()

        val parser = OpenApiParser(resolver)
        val result = parser.parse("/v30/ref-into-another-file/openapi.yaml")

        val validator = Validator()
        val valid = result.validate(validator, store)
        valid.shouldBeTrue()
    }

    /*
    "print validation result" {
        draft4()

        val parser = OpenApiParser(resolver)
        val result = parser.parse("/v30/invalid/openapi.yaml")

        val validator = Validator()
        val valid = result.validate(validator, store)
        valid.shouldBeFalse()

        // detailed
        val printer = ListResultBuilder(FullResultTextBuilder())
        val messages = printer.print(result.validationMessages)
        print(messages)

        println("\n\n")

        // simpler
        val collector = MessageCollector(result.validationMessages)
        val collected = collector.collect()
        val builder = MessageTextBuilder()
        for (message in collected) {
            println(builder.getText(message))
        }
    }*/
})
