/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.*
import io.openapiparser.jackson.JacksonConverter
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class TestBuilder {
    private lateinit var baseUri: URI
    private var api: String? = null

    fun withApi(api: String): TestBuilder {
        return withYaml("file:///any", api.trimIndent())
    }

    fun withYaml(baseUri: String, api: String): TestBuilder {
        this.baseUri = URI(baseUri)
        this.api = api
        return this
    }

    fun withYaml(yml: String): TestBuilder {
        return withYaml("file:///yaml", yml)
    }

    fun withUri(baseURI: URI): TestBuilder {
        this.baseUri = baseURI
        return this
    }

    fun withUri(baseUri: String): TestBuilder {
        this.baseUri = URI(baseUri)
        return this
    }

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
        return OpenApi30(context, context.baseNode)
    }

    fun buildOpenApi31(): OpenApi31 {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry())
        val context = Context(baseUri, resolver)
        context.read()
        return OpenApi31(context, context.baseNode)
    }

    fun <T> build(clazz: Class<T>): T {
        return build { c, n -> clazz
            .getDeclaredConstructor(Context::class.java, Node::class.java)
            .newInstance(c, n)
        }
    }

    fun <T> build(content: String, clazz: Class<T>): T {
        withApi(content)

        return build { c, n -> clazz
            .getDeclaredConstructor(Context::class.java, Node::class.java)
            .newInstance(c, n)
        }
    }

    private fun <T> build(factory: (context: Context, node: Node) -> T): T {
        val resolver = ReferenceResolver(
            baseUri,
            StringReader(api),
            JacksonConverter(),
            ReferenceRegistry()
        )
        val context = Context(baseUri, resolver)
        context.read()
        return factory(context, context.baseNode)
    }

}

fun <T> buildObject(clazz: Class<T>, content: String = "{}"): T {
    return TestBuilder()
        .withApi(content)
        .build(clazz)
}
