/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.support.Uris
import io.openapiprocessor.jsonschema.validator.ValidatorSettingsDefaults
import io.openapiprocessor.jsonschema.validator.steps.SchemaStep
import java.net.URI

class HostnameSpec : StringSpec({

    val settings = ValidatorSettingsDefaults.draft201909()
    val scope = Scope(Uris.emptyUri(), null, SchemaVersion.Draft201909)

    val vocabularies = Vocabularies201909.create(mapOf(
        URI.create(Vocabularies201909.core.uri) to true,
        URI.create(Vocabularies201909.format.uri) to false,
    ))

    val context = JsonSchemaContext(scope, ReferenceRegistry(), vocabularies)

    "provides format as annotation" {
        val hostname = Hostname(settings)

        val schema = JsonSchemaObject(mapOf("format" to "hostname"), context)
        val instance = JsonInstance("any")
        val step = SchemaStep(schema, instance)

        hostname.validate(schema, instance, step)

        step.getAnnotations(Keywords.FORMAT) shouldHaveSingleElement {a -> a.value == "hostname"}
    }
})
