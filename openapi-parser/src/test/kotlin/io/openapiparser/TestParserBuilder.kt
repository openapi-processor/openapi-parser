package io.openapiparser

import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.support.StringReader
import java.net.URI

class TestParserBuilder {
    private var baseUri: URI? = null
    private var api: String? = null

    fun withApi(api: String): TestParserBuilder {
        return withApi("file:///any", api)
    }

    fun withApi(baseUri: String, api: String): TestParserBuilder {
        this.baseUri = URI(baseUri)
        this.api = api
        return this
    }

    fun withUri(baseURI: URI): TestParserBuilder {
        this.baseUri = baseURI
        return this
    }

    fun withUri(baseUri: String): TestParserBuilder {
        this.baseUri = URI(baseUri)
        return this
    }

    fun build(): OpenApiParser {
        val converter = JacksonConverter()
        val resolver = ReferenceResolver(StringReader(api), converter)
        val context = Context(baseUri, resolver)
        return OpenApiParser(context)
    }

}
