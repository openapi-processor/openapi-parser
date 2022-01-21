/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.NoValueException
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.schema.JsonPointer.fromJsonPointer

class JsonPointerSpec : StringSpec({

    "create json pointer from string" {
        data class Pointer(val pointer: String, val expected: Any)

        val source = """
            {
              "foo": ["bar", "baz"],
              "": 0,
              "a/b": 1,
              "c%d": 2,
              "e^f": 3,
              "g|h": 4,
              "i\\j": 5,
              "k\"l": 6,
              " ": 7,
              "m~n": 8
           }
        """.trimIndent()

        val converter = JacksonConverter()
        val document = converter.convert(source)

        listOf(
            Pointer("", document),
            Pointer("/foo", listOf("bar", "baz")),
            Pointer("/foo/0", "bar"),
            Pointer("/", 0),
            Pointer("/a~1b", 1),
            Pointer("/c%d", 2),
            Pointer("/e^f", 3),
            Pointer("/g|h", 4),
            Pointer("""/i\j""", 5),
            Pointer("""/k"l""", 6),
            Pointer("/ ", 7),
            Pointer("/m~0n", 8),
        ).forEach {
            fromJsonPointer(it.pointer).getValue(document) shouldBe it.expected
        }
    }

    "create json pointer from uri fragment" {
        data class Pointer(val pointer: String?, val expected: Any?)

        val source = """
            {
              "foo": ["bar", "baz"],
              "": 0,
              "a/b": 1,
              "c%d": 2,
              "e^f": 3,
              "g|h": 4,
              "i\\j": 5,
              "k\"l": 6,
              " ": 7,
              "m~n": 8
           }
        """.trimIndent()

        val converter = JacksonConverter()
        val document = converter.convert(source)

        listOf(
            Pointer("#", document),
            Pointer("#/foo", listOf("bar", "baz")),
            Pointer("#/foo/0", "bar"),
            Pointer("#/", 0),
            Pointer("#/a~1b", 1),
            Pointer("#/c%25d", 2),
            Pointer("#/e%5Ef", 3),
            Pointer("#/g%7Ch", 4),
            Pointer("#/i%5Cj", 5),
            Pointer("#/k%22l", 6),
            Pointer("#/%20", 7),
            Pointer("#/m~0n", 8),
            Pointer(null, document)
        ).forEach {
            JsonPointer.fromFragment(it.pointer).getValue(document) shouldBe it.expected
        }
    }

    "appends encoded token to json pointer" {
        fromJsonPointer(null).append("/foo").toString() shouldBe "/~1foo"
        fromJsonPointer(null).append("foo").toString() shouldBe "/foo"
        fromJsonPointer("/root").append("/foo").toString() shouldBe "/root/~1foo"
        fromJsonPointer("/root").append("foo").toString() shouldBe "/root/foo"
    }

    "handles null source" {
        JsonPointer.fromFragment(null).toString().shouldBeNull()
        fromJsonPointer(null).toString().shouldBeNull()
    }

    "throws on invalid json pointer" {
        shouldThrow<JsonPointerInvalidException> {
            fromJsonPointer("should/start/with/slash")
        }
    }

    "throws if fragment is invalid" {
        shouldThrow<JsonPointerInvalidException> {
            JsonPointer.fromFragment("#/%XX/invalid")
        }

        shouldThrow<JsonPointerInvalidException> {
            JsonPointer.fromFragment("/missing/hash")
        }
    }

    "append index to pointer" {
        fromJsonPointer("/root").append(1).toString() shouldBe "/root/1"
    }

    "get tail from pointer" {
        JsonPointer.EMPTY.tail() shouldBe ""
        fromJsonPointer("/root").tail() shouldBe "root"
        fromJsonPointer("/root/tail").tail() shouldBe "tail"
    }

    "get tail index from pointer" {
        fromJsonPointer("/root/0").tailIndex() shouldBe 0
    }
})
