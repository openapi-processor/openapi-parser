/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.Context
import io.openapiparser.OpenApiBaseUriProvider
import io.openapiparser.OpenApiParser
import io.openapiparser.OpenApiSchemaDetector
import io.openapiparser.model.v30.setVersion
import io.openapiparser.model.v31.setVersion
import io.openapiparser.model.v32.setVersion
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.support.Types
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31
import io.openapiparser.model.v32.OpenApi as OpenApi32

class ApiBuilder(private var version: SchemaVersion = SchemaVersion.Draft4) {
    private val reader = TestReader()
    private var converter: Converter = SnakeYamlConverter()
    private var documents: DocumentStore = DocumentStore()
    private var loader: DocumentLoader = DocumentLoader(reader, converter)

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

    fun withDocument(uri: String, resourcePath: String): ApiBuilder {
        val document = loader.loadDocument(resourcePath)
        documents.addId(URI(uri), document)
        return this
    }

    fun buildParser(): OpenApiParser {
        return OpenApiParser(documents, DocumentLoader(reader, converter))
    }

    fun buildOpenApi30(uri: String = "file:///openapi.yaml"): OpenApi30 {
        version = SchemaVersion.Draft4
        val api = build(uri, OpenApi30::class.java)
        api.setVersion()
        return api
    }

    fun buildOpenApi31(uri: String = "file:///openapi.yaml"): OpenApi31 {
        version = SchemaVersion.Draft202012
        val api = build(uri, OpenApi31::class.java)
        api.setVersion()
        return api
    }

    fun buildOpenApi32(uri: String = "file:///openapi.yaml"): OpenApi32 {
        version = SchemaVersion.Draft202012
        val api = build32(uri, OpenApi32::class.java)
        api.setVersion()
        return api
    }

    fun <T> build(source: String, clazz: Class<T>): T {
        return build(source) { ctx, bkt -> clazz
            .getDeclaredConstructor(Context::class.java, Bucket::class.java)
            .newInstance(ctx, bkt)
        }
    }

    fun <T> build32(source: String, clazz: Class<T>): T {
        return build32(source) { ctx, bkt -> clazz
            .getDeclaredConstructor(Context::class.java, Bucket::class.java)
            .newInstance(ctx, bkt)
        }
    }

    private fun <T> build32(uri: String, factory: (context: Context, bucket: Bucket) -> T): T {
        var source = URI(uri)
        if (!source.isAbsolute) {
           source =  this::class.java.getResource(uri)!!.toURI()
        }

        val resolver = createResolver()
        val settings = Resolver.Settings(version)
            .schemaDetector(OpenApiSchemaDetector())
            .baseUriProvider(OpenApiBaseUriProvider())
        val document = documents.get(source)

        val result = if (document != null) {
            resolver.resolve(source, document, settings)
        } else {
            resolver.resolve(source, settings)
        }

        return factory(
            Context(result.scope, result.registry),
            Bucket(result.scope, Types.asMap(result.document)!!))
    }

    private fun <T> build(uri: String, factory: (context: Context, bucket: Bucket) -> T): T {
        var source = URI(uri)
        if (!source.isAbsolute) {
           source =  this::class.java.getResource(uri)!!.toURI()
        }

        val resolver = createResolver()
        val document = documents.get(source)
        val settings = Resolver.Settings(version).schemaDetector(OpenApiSchemaDetector())
        val result = if (document != null) {
            resolver.resolve(source, document, settings)
        } else {
            resolver.resolve(source, settings)
        }

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
fun <T> buildObject(clazz: Class<T>, content: String = "{}", version: SchemaVersion = SchemaVersion.Draft4): T {
    return ApiBuilder(version)
        .withApi(content)
        .build("file:///openapi.yaml", clazz)
}

fun <T> buildObject30(clazz: Class<T>, content: String = "{}"): T {
    return buildObject(clazz, content,SchemaVersion.Draft4)
}

fun <T> buildObject31(clazz: Class<T>, content: String = "{}"): T {
    return buildObject(clazz, content,SchemaVersion.Draft202012)
}

fun <T> buildObject32(clazz: Class<T>, content: String = "{}"): T {
    return buildObject(clazz, content,SchemaVersion.Draft202012)
}

class OpenApi
