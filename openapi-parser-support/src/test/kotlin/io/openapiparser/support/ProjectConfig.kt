/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.core.config.AbstractProjectConfig
import io.openapiparser.memory.MemoryUrlStreamHandlerFactory

/**
 * kotest config
 */
object ProjectConfig: AbstractProjectConfig() {
    init {
        // register memory: protocol
        MemoryUrlStreamHandlerFactory.register()
    }
}
