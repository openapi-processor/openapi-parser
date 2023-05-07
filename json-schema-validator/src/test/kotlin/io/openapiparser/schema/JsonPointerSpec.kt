/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.Types.asMap
import io.openapiparser.jackson.JacksonConverter
import java.net.URI

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
        val document = asMap(converter.convert(source))

        listOf(
            Pointer("", document!!),
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
            Bucket(document).getRawValueValue(JsonPointer.from(it.pointer)) shouldBe it.expected
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
        val document = asMap(converter.convert(source))

        listOf(
            Pointer("#", document!!),
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
            Bucket(document).getRawValueValue(JsonPointer.from(it.pointer)) shouldBe it.expected
        }
    }

    "appends encoded token to json pointer" {
        JsonPointer.from(null).append("/foo").toString() shouldBe "/~1foo"
        JsonPointer.from(null).append("foo").toString() shouldBe "/foo"
        JsonPointer.from("/root").append("/foo").toString() shouldBe "/root/~1foo"
        JsonPointer.from("/root").append("foo").toString() shouldBe "/root/foo"
    }

    "handles null source" {
        JsonPointer.from(null).toString().shouldBe("")
    }

    "throws on invalid json pointer" {
        shouldThrow<JsonPointerInvalidException> {
            JsonPointer.from("should/start/with/slash")
        }
    }

    "throws if fragment is invalid" {
        shouldThrow<JsonPointerInvalidException> {
            JsonPointer.from("#/%XX/invalid")
        }
    }

    "append index to pointer" {
        JsonPointer.from("/root").append(1).toString() shouldBe "/root/1"
    }

    "get empty tail from pointer" {
        JsonPointer.from("/root/").tail() shouldBe ""
    }


    "get tail from pointer" {
        JsonPointer.EMPTY.tail() shouldBe ""
        JsonPointer.from("/root").tail() shouldBe "root"
        JsonPointer.from("/root/tail").tail() shouldBe "tail"
    }

    "get tail index from pointer" {
        JsonPointer.from("/root/0").tailIndex() shouldBe 0
    }

    "get tail index from pointer throws if index is no int" {
        shouldThrow<JsonPointerInvalidException> {
            JsonPointer.from("/root/boom").tailIndex()
        }
    }

    "converts to uri" {
        JsonPointer.from("").toUri() shouldBe URI.create("")
        JsonPointer.from("/").toUri() shouldBe URI.create("#/")
        JsonPointer.from("/0").toUri() shouldBe URI.create("#/0")
        JsonPointer.from("/to/ke/ns").toUri() shouldBe URI.create("#/to/ke/ns")
        // todo ~ encoding
        // todo uri encoding
    }
})
