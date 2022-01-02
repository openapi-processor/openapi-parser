/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.*
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.reader.StringReader
import io.openapiparser.schema.Bucket
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class TestBuilder {
    private lateinit var baseUri: URI
    private var api: String? = null

    fun withApi(api: String): TestBuilder {
        return withYaml("file:///any", api.trimIndent())
    }

    fun withYaml(yml: String): TestBuilder {
        return withYaml("file:///yaml", yml)
    }

    fun withYaml(baseUri: String, api: String): TestBuilder {
        this.baseUri = URI(baseUri)
        this.api = api
        return this
    }

//    fun withUri(baseURI: URI): TestBuilder {
//        this.baseUri = baseURI
//        return this
//    }

//    fun withUri(baseUri: String): TestBuilder {
//        this.baseUri = URI(baseUri)
//        return this
//    }

    fun buildParser(): OpenApiParser {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry())
        val context = Context(baseUri, resolver)
        return OpenApiParser(context)
    }

    fun buildContext(): Context {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry())
        val context = Context(baseUri, resolver)
        context.read()
        return context
    }

    fun buildOpenApi30(): OpenApi30 {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry())
        val context = Context(baseUri, resolver)
        context.read()
        return OpenApi30(context, context.bucket)
    }

    fun buildOpenApi31(): OpenApi31 {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry())
        val context = Context(baseUri, resolver)
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
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry()
        )
        val context = Context(baseUri, resolver)
        context.read()
        return factory(context, context.bucket)
    }
}

/**
 * create a specific model object of [clazz] with [content].
 *
 * @param clazz model object to build
 * @param content the object properties (yaml)
 */
fun <T> buildObject(clazz: Class<T>, content: String = "{}"): T {
    return TestBuilder()
        .withApi(content)
        .build(clazz)
}
