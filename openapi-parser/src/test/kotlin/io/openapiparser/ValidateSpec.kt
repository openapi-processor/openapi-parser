/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.SchemaStore
import io.openapiparser.snakeyaml.SnakeYamlConverter
import io.openapiparser.support.ApiBuilder
import io.openapiparser.validator.Validator
import java.net.URI

class ValidateSpec: StringSpec({

    "validate" {
        val parser = ApiBuilder()
            .withResource("/v30/ref-into-another-file/openapi.yaml")
            .buildParser()

        val result = parser.parse()
        val validator = Validator()

        val store = SchemaStore(UriReader(), SnakeYamlConverter())
        store.addSchema(
            URI.create("http://json-schema.org/draft-04/schema#"),
            "/json-schema/draft-04/schema.json")

        val x = result.validate(validator, store)

        x.shouldBeTrue()
    }

})
