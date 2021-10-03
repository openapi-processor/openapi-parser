package io.openapiparser.support

import io.openapiparser.Context
import io.openapiparser.Node
import io.openapiparser.OpenApiParser
import io.openapiparser.ReferenceResolver
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31
import io.openapiparser.jackson.JacksonConverter
import java.net.URI

class TestBuilder {
    private var baseUri: URI? = null
    private var api: String? = null

    fun withApi(api: String): TestBuilder {
        return withApi("file:///any", api)
    }

    fun withApi(baseUri: String, api: String): TestBuilder {
        this.baseUri = URI(baseUri)
        this.api = api
        return this
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
        val converter = JacksonConverter()
        val resolver = ReferenceResolver(StringReader(api), converter)
        val context = Context(baseUri, resolver)
        return OpenApiParser(context)
    }

    fun buildContext(): Context {
        val converter = JacksonConverter()
        val resolver = ReferenceResolver(StringReader(api), converter)
        val context = Context(baseUri, resolver)
        context.read()
        return context
    }

    fun buildOpenApi30(): OpenApi30 {
        val converter = JacksonConverter()
        val resolver = ReferenceResolver(StringReader(api), converter)
        val context = Context(baseUri, resolver)
        context.read()
        return OpenApi30(context, context.baseNode)
    }

    fun buildOpenApi31(): OpenApi31 {
        val converter = JacksonConverter()
        val resolver = ReferenceResolver(StringReader(api), converter)
        val context = Context(baseUri, resolver)
        context.read()
        return OpenApi31(context, context.baseNode)
    }

}
