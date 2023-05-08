/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.schema.SchemaKeywords

class SchemaKeywordsSpec : StringSpec({

    data class Keyword(val keyword: String, val navigatable: Boolean)
    val keywordsDraft6 = SchemaKeywords.draft6

    withData(
        Keyword("\$schema", false),
        Keyword("\$id", false),
        Keyword("multipleOf", false),
        Keyword("maximum", false),
        Keyword("minimum", false),
        Keyword("exclusiveMaximum", false),
        Keyword("exclusiveMinimum", false),
        Keyword("maxLength", false),
        Keyword("minLength", false),
        Keyword("pattern", false),
        Keyword("additionalItems", true),
        Keyword("items", true),
        Keyword("maxItems", false),
        Keyword("minItems", false),
        Keyword("uniqueItems", false),
        Keyword("contains", true),
        Keyword("maxProperties", false),
        Keyword("minProperties", false),
        Keyword("required", false),
        Keyword("additionalProperties", true),
        Keyword("properties", true),
        Keyword("patternProperties", true),
        Keyword("dependencies", true),
        Keyword("propertyNames", true),
        Keyword("enum", false),
        Keyword("const", false),
        Keyword("type", false),
        Keyword("allOf", true),
        Keyword("anyOf", true),
        Keyword("oneOf", true),
        Keyword("not", true),
        Keyword("definitions", true),
        Keyword("title", false),
        Keyword("description", false),
        Keyword("default", false),
        Keyword("examples", false),
        Keyword("format", false)
    ) { (keyword, navigatable) ->
        keywordsDraft6.isNavigable (keyword) shouldBe navigatable
    }
})
