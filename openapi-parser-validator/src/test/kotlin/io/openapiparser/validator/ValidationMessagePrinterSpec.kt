/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.schema.*
import java.net.URI

class ValidationMessagePrinterSpec : StringSpec({

    val sctx = JsonSchemaContext(URI(""), ReferenceRegistry(), SchemaVersion.Default)
    val schema = JsonSchemaObject(mapOf(), sctx)

    val ictx = JsonInstanceContext(URI(""), ReferenceRegistry())
    val instance = JsonInstance("value", ictx)


    val printer = ValidationMessagePrinter()

    "only root items" {
        val msgs = listOf(
            ValidationMessage(schema, instance, "foo1"),
            ValidationMessage(schema, instance, "foo2"),
            ValidationMessage(schema, instance, "foo3")
        )

        val output = printer.print(msgs)

        output shouldBe """
            foo1
            foo2
            foo3
            
        """.trimIndent()
    }

    "item with sub item" {
        val msgs = listOf(
            ValidationMessage(schema, instance, "foo1", listOf(
                ValidationMessage(schema, instance, "foo11")
            ))
        )

        val output = printer.print(msgs)

        output shouldBe """
            foo1
            \--- foo11
            
        """.trimIndent()
    }

    "item with sub items" {
        val msgs = listOf(
            ValidationMessage(schema, instance, "foo1", listOf(
                ValidationMessage(schema, instance, "foo11"),
                ValidationMessage(schema, instance, "foo12"),
                ValidationMessage(schema, instance, "foo13")
            ))
        )

        val output = printer.print(msgs)

        output shouldBe """
            foo1
            +--- foo11
            +--- foo12
            \--- foo13
            
        """.trimIndent()
    }

    "item with deeply nested sub items"  {
        val msgs = listOf(
            ValidationMessage(schema, instance, "foo1", listOf(
                ValidationMessage(schema, instance, "foo2", listOf(
                    ValidationMessage(schema, instance, "foo3", listOf(
                        ValidationMessage(schema, instance, "foo4", listOf(
                            ValidationMessage(schema, instance, "foo5", listOf(
                                ValidationMessage(schema, instance, "foo6"),
                            ))
                        ))
                    ))
                ))
            ))
        )

        val output = printer.print(msgs)

        output shouldBe """
                foo1
                \--- foo2
                     \--- foo3
                          \--- foo4
                               \--- foo5
                                    \--- foo6
                
            """.trimIndent()
    }

    "item with multiple nested sub items" {
        val msgs = listOf(
            ValidationMessage(
                schema, instance, "foo1", listOf(
                    ValidationMessage(
                        schema, instance, "foo11", listOf(
                            ValidationMessage(schema, instance, "foo111"),
                            ValidationMessage(schema, instance, "foo112")
                        )
                    ),
                    ValidationMessage(
                        schema, instance, "foo12", listOf(
                            ValidationMessage(schema, instance, "foo121", listOf(
                                ValidationMessage(schema, instance, "foo1211"),
                                ValidationMessage(schema, instance, "foo1212")
                            )),
                            ValidationMessage(schema, instance, "foo122")
                        )
                    ),
                    ValidationMessage(
                        schema, instance, "foo13", listOf(
                            ValidationMessage(schema, instance, "foo131"),
                            ValidationMessage(schema, instance, "foo132")
                        )
                    )
                )
            )
        )

        val output = printer.print(msgs)

        output shouldBe """
            foo1
            +--- foo11
            |    +--- foo111
            |    \--- foo112
            +--- foo12
            |    +--- foo121
            |    |    +--- foo1211
            |    |    \--- foo1212
            |    \--- foo122
            \--- foo13
                 +--- foo131
                 \--- foo132
            
        """.trimIndent()
    }
})

