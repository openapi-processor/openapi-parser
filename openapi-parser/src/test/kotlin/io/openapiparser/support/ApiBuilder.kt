/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.*
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.reader.StringReader
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.Bucket
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class ApiBuilder {
    private var api: String? = null
    private lateinit var apiUri: URI

    fun withApi(api: String): ApiBuilder {
        return withYaml("file:///any", api.trimIndent())
    }

    fun withApi(api: URI): ApiBuilder {
        this.apiUri = api
        return this
    }

    fun withResource(api: String): ApiBuilder {
        this.apiUri = this::class.java.getResource(api)!!.toURI()
        return this
    }

    fun buildParser(): OpenApiParser {
        return OpenApiParser(createContext())
    }

    fun buildOpenApi30(): OpenApi30 {
        val context = createContext()
        context.read()
        return OpenApi30(context, context.bucket)
    }

    fun buildOpenApi31(): OpenApi31 {
        val context = createContext()
        context.read()
        return OpenApi31(context, context.bucket)
    }

    fun <T> build(clazz: Class<T>): T {
        return build { c, n -> clazz
            .getDeclaredConstructor(Context::class.java, Bucket::class.java)
            .newInstance(c, n)
        }
    }

    private fun <T> build(factory: (context: Context, bucket: Bucket) -> T): T {
        val context = createContext()
        context.read()
        return factory(context, context.bucket)
    }

    private fun createContext(): Context {
        apiUri.shouldNotBeNull()

        val resolver = ReferenceResolver(
            apiUri,
            getReader(),
            JacksonConverter(),
            ReferenceRegistry()
        )
        return Context(apiUri, resolver)
    }

    private fun withYaml(baseUri: String, api: String): ApiBuilder {
        this.apiUri = URI(baseUri)
        this.api = api
        return this
    }

    private fun getReader(): Reader {
        if (api == null) {
            return UriReader()
        }
        return StringReader(api)
    }
}

/**
 * create a specific model object of [clazz] with [content].
 *
 * @param clazz model object to build
 * @param content the object properties (yaml)
 */
fun <T> buildObject(clazz: Class<T>, content: String = "{}"): T {
    return ApiBuilder()
        .withApi(content)
        .build(clazz)
}
