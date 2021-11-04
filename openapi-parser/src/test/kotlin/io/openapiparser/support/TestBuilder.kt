/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.*
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.model.v30.Parameter
import io.openapiparser.model.v30.PathItem
import java.net.URI

class TestBuilder {
    private var baseUri: URI? = null
    private var api: String? = null

    fun withApi(api: String): TestBuilder {
        return withYaml("file:///any", api)
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

    fun buildPathItem (): PathItem {
        return build { ctx, node -> PathItem(ctx, node) }
    }

    fun buildParameter (): Parameter {
        return build { ctx, node -> Parameter(ctx, node) }
    }

    fun <T> build(factory: (context: Context, node: Node) -> T): T {
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
