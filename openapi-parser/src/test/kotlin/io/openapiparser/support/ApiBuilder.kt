/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.*
import io.openapiparser.converter.Types
import io.openapiparser.reader.StringReader
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.Bucket
import io.openapiparser.schema.DocumentStore
import io.openapiparser.schema.Resolver
import io.openapiparser.snakeyaml.SnakeYamlConverter
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class ApiBuilder {
    private var api: String? = null
    private lateinit var apiUri: URI
    private var converter: Converter = SnakeYamlConverter()
    private var documents: DocumentStore = DocumentStore()

    fun withApi(api: String): ApiBuilder {
        return withYaml("file:///any", api.trimIndent())
    }

    fun withApi(api: URI): ApiBuilder {
        apiUri = api
        return this
    }

    fun withResource(api: String): ApiBuilder {
        apiUri = this::class.java.getResource(api)!!.toURI()
        return this
    }

    fun withConverter(converter: Converter): ApiBuilder {
        this.converter = converter
        return this
    }

    fun buildParser(): OpenApiParser {
        return OpenApiParser(createResolver())
    }

    fun buildOpenApi30(): OpenApi30 {
        val resolver = createResolver()
        val result = resolver.resolve(apiUri)

        return OpenApi30(
            Context(result.uri, result.registry),
            Bucket(result.uri, Types.asMap(result.document)!!)
        )
    }

    fun buildOpenApi31(): OpenApi31 {
        val resolver = createResolver()
        val result = resolver.resolve(apiUri)

        return OpenApi31(
            Context(result.uri, result.registry),
            Bucket(result.uri, Types.asMap(result.document)!!)
        )
    }

    fun <T> build(clazz: Class<T>): T {
        return build { c, n -> clazz
            .getDeclaredConstructor(Context::class.java, Bucket::class.java)
            .newInstance(c, n)
        }
    }

    private fun <T> build(factory: (context: Context, bucket: Bucket) -> T): T {
        val resolver = createResolver()
        val result = resolver.resolve(apiUri)

        return factory(
            Context(result.uri, result.registry),
            Bucket(result.uri, Types.asMap(result.document)!!)
        )
    }

    private fun createResolver(): Resolver {
        return Resolver(getReader(), converter, documents)
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
        return StringReader(api!!)
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
