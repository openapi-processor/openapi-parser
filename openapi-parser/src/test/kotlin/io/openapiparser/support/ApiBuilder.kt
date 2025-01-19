/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.*
import io.openapiprocessor.jsonschema.support.Types
import io.openapiprocessor.jsonschema.schema.Bucket
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.DocumentStore
import io.openapiprocessor.jsonschema.schema.Resolver
import io.openapiprocessor.jsonschema.schema.SchemaVersion
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.jsonschema.schema.ResolverResult
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class ApiBuilder {
    private val reader = TestReader()
    private var converter: Converter = SnakeYamlConverter()
    private var documents: DocumentStore = DocumentStore()

    fun withApi(apiYaml: String): ApiBuilder {
        reader.addApi(URI("file:///openapi.yaml"), apiYaml.trimIndent())
        return this
    }

    fun withApi(uri: String, apiYaml: String): ApiBuilder {
        reader.addApi(URI(uri), apiYaml.trimIndent())
        return this
    }

    fun withConverter(converter: Converter): ApiBuilder {
        this.converter = converter
        return this
    }

    fun withDocument(document: Any): ApiBuilder {
        documents.addId(URI("file:///openapi.yaml"), document)
        return this
    }

    fun withDocument(uri: URI, document: Any): ApiBuilder {
        documents.addId(uri, document)
        return this
    }

    fun buildParser(): OpenApiParser {
        return OpenApiParser(documents, DocumentLoader(reader, converter))
    }

    fun buildOpenApi30(): OpenApi30 {
        return buildOpenApi30("file:///openapi.yaml")
    }

    fun buildOpenApi30(uri: String): OpenApi30 {
        var source = URI(uri)
        if (!source.isAbsolute) {
           source =  this::class.java.getResource(uri)!!.toURI()
        }

        val resolver = createResolver()
        val result = resolver.resolve(source, Resolver.Settings(SchemaVersion.Draft4))

        return OpenApi30(
            Context(result.scope, result.registry),
            Bucket(result.scope, Types.asMap(result.document)!!))
    }


    fun buildOpenApi31(): OpenApi31 {
        return buildOpenApi31("file:///openapi.yaml")
    }

    fun buildOpenApiRaw31(uri: String): Any {
        var source = URI(uri)
        if (!source.isAbsolute) {
           source =  this::class.java.getResource(uri)!!.toURI()
        }

        val resolver = createResolver()
        val result = resolver.resolve(source, Resolver.Settings(SchemaVersion.Draft202012))

        return result.document
    }

    fun buildOpenApi31(uri: String): OpenApi31 {
        var source = URI(uri)
        if (!source.isAbsolute) {
           source =  this::class.java.getResource(uri)!!.toURI()
        }

        val resolver = createResolver()
        val document = documents.get(source)
        val result: ResolverResult = if (document != null) {
            resolver.resolve(source, document, Resolver.Settings(SchemaVersion.Draft202012))
        } else {
            resolver.resolve(source, Resolver.Settings(SchemaVersion.Draft202012))
        }

        return OpenApi31(
            Context(result.scope, result.registry),
            Bucket(result.scope, Types.asMap(result.document)!!))
    }

    fun <T> build(clazz: Class<T>): T {
        return build30 { c, n -> clazz
            .getDeclaredConstructor(Context::class.java, Bucket::class.java)
            .newInstance(c, n)
        }
    }

    private fun <T> build30(factory: (context: Context, bucket: Bucket) -> T): T {
        val resolver = createResolver()
        val result = resolver.resolve(URI("file:///openapi.yaml"), Resolver.Settings(SchemaVersion.Draft4))

        return factory(
            Context(result.scope, result.registry),
            Bucket(result.scope, Types.asMap(result.document)!!))
    }

    private fun createResolver(): Resolver {
        return Resolver(documents, DocumentLoader(reader, converter))
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
