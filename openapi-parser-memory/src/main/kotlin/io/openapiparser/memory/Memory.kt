/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.memory

/**
 * in-memory content by path.
 */
class Memory {
    companion object {
        private val contents: HashMap<String, ByteArray> = hashMapOf()

        fun get(path: String): ByteArray {
            return contents[path] ?: throw Exception()
        }

        fun add(path: String, data: String) {
            add(path, data.toByteArray(Charsets.UTF_8))
        }

        fun clear() {
            contents.clear()
        }

        private fun add(path: String, data: ByteArray) {
            contents[path] = data
        }
    }
}
