/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URI

class RefSpec: StringSpec({
    data class Data(val ref: Ref, val document: URI, val absoluteRef: URI)

    val refs = listOf(
        Data(Ref("#"),
            URI(""),
            URI("#")
        ),
        Data(Ref("#/foo"),
            URI(""),
            URI("#/foo")
        ),
        Data(Ref("path#/foo"),
            URI("path"),
            URI("path#/foo")
        ),
        Data(Ref("#foo"), // not a json pointer !
            URI("#foo"),
            URI("#foo")
        ),
        Data(Ref("http://json-schema.org/draft-04/schema#"),
            URI("http://json-schema.org/draft-04/schema"),
            URI("http://json-schema.org/draft-04/schema#"),
        ),
        Data(Ref(
            "http://json-schema.org/draft-04/schema#",
            "http://json-schema.org/draft-04/schema#"),
            URI("http://json-schema.org/draft-04/schema"),
            URI("http://json-schema.org/draft-04/schema#"),
        ),
        Data(Ref("http://json-schema.org/draft-04/schema#", "#"),
            URI("http://json-schema.org/draft-04/schema"),
            URI("http://json-schema.org/draft-04/schema#")
        ),
        Data(Ref("http://json-schema.org/draft-04/schema#", "#/definitions/stringArray"),
            URI("http://json-schema.org/draft-04/schema"),
            URI("http://json-schema.org/draft-04/schema#/definitions/stringArray"),
        )
    )

    refs.forEachIndexed { idx, it ->
        "document uri of '${it.ref.ref}' should be '${it.document}' ($idx)" {
            it.ref.documentUri shouldBe it.document
        }
    }

    refs.forEachIndexed { idx, it ->
        "absolute ref uri of '${it.ref.ref}' should be '${it.absoluteRef}' ($idx)" {
            it.ref.fullRefUri shouldBe it.absoluteRef
        }
    }

})

