/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.openapiprocessor.jsonschema.support.UriSupport.emptyFragment
import io.openapiprocessor.jsonschema.support.UriSupport.emptyUri

class ReferenceRegistrySpec : StringSpec({
    val dummyScope = Scope(
        emptyUri(),
        emptyUri(),
        SchemaVersion.Draft4
    )

    "find reference with empty fragment" {
        val registry = ReferenceRegistry()

        val ref = Ref(dummyScope, "")
        registry.addReference(ref, dummyScope, emptyMap<String, Any>())

        registry.hasReference(emptyFragment()) shouldBe true
        registry.getReference(emptyFragment()) shouldNotBe null
    }

})
