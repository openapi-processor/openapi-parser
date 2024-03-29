/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema

import io.kotest.core.Tag
import io.kotest.core.TagExpression
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.TagExtension
import io.openapiparser.memory.MemoryUrlStreamHandlerFactory

object Windows: Tag()
object NotWindows: Tag()

object SystemTagExtension: TagExtension {

    override fun tags(): TagExpression {

        return if(isWindows()) {
            TagExpression.exclude(NotWindows)
        } else {
            TagExpression.exclude(Windows)
        }
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name")
            .lowercase()
            .contains("windows")
    }

}

/**
 * kotest config
 */
object ProjectConfig: AbstractProjectConfig() {
    init {
         // register memory: protocol
         MemoryUrlStreamHandlerFactory.register()
    }

    override fun extensions() = listOf(SystemTagExtension)
}
