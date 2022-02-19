/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.net.URI

class RefSpec: StringSpec({

    "should provide the document uri of a ref with scope" {
        forAll(
            row(Ref("#/foo"), URI("")),
            row(Ref("path#/foo"), URI("path")),
            row(Ref("", "http://json-schema.org/draft-04/schema#"),
                URI("http://json-schema.org/draft-04/schema")),
            row(Ref("http://json-schema.org/draft-04/schema#",
                "http://json-schema.org/draft-04/schema#"),
                URI("http://json-schema.org/draft-04/schema")),
            row(Ref("http://json-schema.org/draft-04/schema#", "#"),
                URI("http://json-schema.org/draft-04/schema"))
        ) { ref: Ref, expected: URI ->
            ref.documentUri.shouldBe(expected)
        }
    }

})

