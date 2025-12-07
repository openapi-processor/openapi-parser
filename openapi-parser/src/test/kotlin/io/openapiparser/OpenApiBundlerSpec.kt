/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jackson.JacksonConverter
import io.openapiprocessor.jsonschema.support.Types.asObject
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.JsonPointer.from
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.snakeyaml.SnakeYamlWriter
import java.io.StringWriter

class OpenApiBundlerSpec : FreeSpec({

    fun resolve(documentUri: String): ResolverResult {
        val documents = DocumentStore()
        val loader = DocumentLoader(UriReader(), SnakeYamlConverter())
        val resolver = Resolver(documents, loader)
        return resolver.resolve(documentUri, Resolver.Settings(SchemaVersion.Draft4))
    }

    fun bundle30(result: ResolverResult): Bucket {
        val context = Context(result.scope, result.registry)
        val bucket = Bucket(result.scope, asObject(result.document))
        val api = OpenApiResult30(context, bucket, result.documents)
        val bundle = api.bundle()
        return Bucket.createBucket(result.scope, bundle)!!
    }

    fun bundle31(result: ResolverResult): Bucket {
        val context = Context(result.scope, result.registry)
        val bucket = Bucket(result.scope, asObject(result.document))
        val api = OpenApiResult31(context, bucket, result.documents)
        val bundle = api.bundle()
        return Bucket.createBucket(result.scope, bundle)!!
    }

    fun bundle32(result: ResolverResult): Bucket {
        val context = Context(result.scope, result.registry)
        val bucket = Bucket(result.scope, asObject(result.document))
        val api = OpenApiResult32(context, bucket, result.documents)
        val bundle = api.bundle()
        return Bucket.createBucket(result.scope, bundle)!!
    }

    fun getObject(bucket: Bucket, jsonPointer: String): Map<String, Any?> {
        return asObject(bucket.getRawValue(from(jsonPointer))!!.value)
    }

    data class Data(val api: String, val bundle: (result: ResolverResult) -> Bucket)

    "do not walk array on simple type" - {
        val context = Context(Scope.empty(), ReferenceRegistry())

        val converter = JacksonConverter()
        val document = converter.convert("""
                    openapi: 3.0.2
                    info:
                      title: do not walk tags
                      version: 1.0.0
        
                    paths:
                      /foo:
                        get:
                          tags:
                            - a tag
                          responses:
                            '204':
                              description: none
                    """.trimIndent()
                )

        val documents = DocumentStore()
        val scope = Scope.empty()
        documents.addId(scope.documentUri, document)

        val bucket = Bucket.createBucket(scope, document)

        shouldNotThrowAny {
            OpenApiBundler(context, documents, bucket!!).bundle()
        }
    }

    withData(
        mapOf(
            $$"bundle handles $ref loop, 30" to Data("/bundle-ref-loop/openapi30.yaml", ::bundle30),
            $$"bundle handles $ref loop, 31" to Data("/bundle-ref-loop/openapi31.yaml", ::bundle31),
            $$"bundle handles $ref loop, 32" to Data("/bundle-ref-loop/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1self-reference/get/responses/200/content/application~1json/schema")
        ref[$$"$ref"].shouldBe("#/components/schemas/Self")

        val schema = getObject(bundled, "/components/schemas/Self/properties/self")
        schema[$$"$ref"].shouldBe("#/components/schemas/Self")
    }

    withData(
        mapOf(
            $$"bundle schema $ref, 30" to Data("/bundle-ref-schema/openapi30.yaml", ::bundle30),
            $$"bundle schema $ref, 31" to Data("/bundle-ref-schema/openapi31.yaml", ::bundle31),
            $$"bundle schema $ref, 32" to Data("/bundle-ref-schema/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200/content/application~1json/schema")
        ref[$$"$ref"].shouldBe("#/components/schemas/Foo")

        val component = bundled.getRawValue(from("/components/schemas/Foo"))
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle response $ref, 30" to Data("/bundle-ref-response/openapi30.yaml", ::bundle30),
            $$"bundle response $ref, 31" to Data("/bundle-ref-response/openapi31.yaml", ::bundle31),
            $$"bundle response $ref, 32" to Data("/bundle-ref-response/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/responses/Foo")

        val component = getObject(bundled, "/components/responses/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle parameter $ref, 30" to Data("/bundle-ref-parameter/openapi30.yaml", ::bundle30),
            $$"bundle parameter $ref, 31" to Data("/bundle-ref-parameter/openapi31.yaml", ::bundle31),
            $$"bundle parameter $ref, 32" to Data("/bundle-ref-parameter/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/parameters/0")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/parameters/Foo")

        val component = getObject(bundled, "/components/parameters/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle example $ref, 30" to Data("/bundle-ref-example/openapi30.yaml", ::bundle30),
            $$"bundle example $ref, 31" to Data("/bundle-ref-example/openapi31.yaml", ::bundle31),
            $$"bundle example $ref, 32" to Data("/bundle-ref-example/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/parameters/0/examples/foo")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/examples/Foo")

        val component = getObject(bundled, "/components/examples/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle request body $ref, 30" to Data("/bundle-ref-request-body/openapi30.yaml", ::bundle30),
            $$"bundle request body $ref, 31" to Data("/bundle-ref-request-body/openapi31.yaml", ::bundle31),
            $$"bundle request body $ref, 32" to Data("/bundle-ref-request-body/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/requestBody")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/requestBodies/Foo")

        val component = getObject(bundled, "/components/requestBodies/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle header $ref, 30" to Data("/bundle-ref-header/openapi30.yaml", ::bundle30),
            $$"bundle header $ref, 31" to Data("/bundle-ref-header/openapi31.yaml", ::bundle31),
            $$"bundle header $ref, 32" to Data("/bundle-ref-header/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/204/headers/foo")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/headers/Foo")

        val component = getObject(bundled, "/components/headers/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle security scheme $ref, 30" to Data("/bundle-ref-security-scheme/openapi30.yaml", ::bundle30),
            $$"bundle security scheme $ref, 31" to Data("/bundle-ref-security-scheme/openapi31.yaml", ::bundle31),
            $$"bundle security scheme $ref, 32" to Data("/bundle-ref-security-scheme/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val scheme = getObject(bundled, "/components/securitySchemes/jwt")
        scheme.containsKey(Keywords.REF).shouldBeFalse()
        scheme.containsKey("type").shouldBeTrue()
    }

    withData(
        mapOf(
            $$"bundle link $ref, 30" to Data("/bundle-ref-link/openapi30.yaml", ::bundle30),
            $$"bundle link $ref, 31" to Data("/bundle-ref-link/openapi31.yaml", ::bundle31),
            $$"bundle link $ref, 32" to Data("/bundle-ref-link/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/204/links/foo")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/links/Foo")

        val component = getObject(bundled, "/components/links/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle callback $ref, 30" to Data("/bundle-ref-callback/openapi30.yaml", ::bundle30),
            $$"bundle callback $ref, 31" to Data("/bundle-ref-callback/openapi31.yaml", ::bundle31),
            $$"bundle callback $ref, 32" to Data("/bundle-ref-callback/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, $$"/paths/~1foo/get/callbacks/$url")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/callbacks/Foo")

        val component = getObject(bundled, "/components/callbacks/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle path $ref, 30" to Data("/bundle-ref-path-item/openapi30.yaml", ::bundle30),
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val out = StringWriter()
        val writer = SnakeYamlWriter(out)
        writer.write(bundled.rawValues)

        val pathItem = getObject(bundled, "/paths/~1foo")
        pathItem.containsKey(Keywords.REF).shouldBeFalse()
        pathItem.containsKey("get").shouldBeTrue()
    }

    withData(
        mapOf(
            $$"bundle path $ref, 31" to Data("/bundle-ref-path-item/openapi31.yaml", ::bundle31),
            $$"bundle path $ref, 32" to Data("/bundle-ref-path-item/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val out = StringWriter()
        val writer = SnakeYamlWriter(out)
        writer.write(bundled.rawValues)

        val ref = getObject(bundled, "/paths/~1foo")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/pathItems/_bundle-ref-path-item_foo.api.yaml")

        val component = getObject(bundled, "/components/pathItems/_bundle-ref-path-item_foo.api.yaml")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle nested $ref, 30" to Data("/bundle-ref-nested/openapi30.yaml", ::bundle30)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val out = StringWriter()
        val writer = SnakeYamlWriter(out)
        writer.write(bundled.rawValues)

        val pathItem = getObject(bundled, "/paths/~1foo")
        pathItem.containsKey(Keywords.REF).shouldBeFalse()
        pathItem.containsKey("get").shouldBeTrue()

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200/content/application~1json/schema")
        ref[$$"$ref"].shouldBe("#/components/schemas/Bar")
        val component = bundled.getRawValue(from("/components/schemas/Bar"))
        component.shouldNotBeNull()

        val refNested = getObject(bundled, "/components/schemas/Bar/properties/bar")
        refNested[$$"$ref"].shouldBe("#/components/schemas/Bar2")
        val componentNested = bundled.getRawValue(from("/components/schemas/Bar2"))
        componentNested.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle nested $ref, 31" to Data("/bundle-ref-nested/openapi31.yaml", ::bundle31),
            $$"bundle nested $ref, 32" to Data("/bundle-ref-nested/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val out = StringWriter()
        val writer = SnakeYamlWriter(out)
        writer.write(bundled.rawValues)

        val ref = getObject(bundled, "/paths/~1foo")
        ref.size shouldBe 1
        ref[$$"$ref"].shouldBe("#/components/pathItems/_bundle-ref-nested_foo.api.yaml")

        val component = getObject(bundled, "/components/pathItems/_bundle-ref-nested_foo.api.yaml")
        component.shouldNotBeNull()

        val barRef = getObject(bundled, "/components/pathItems/_bundle-ref-nested_foo.api.yaml/get/responses/200/content/application~1json/schema")
        barRef[$$"$ref"].shouldBe("#/components/schemas/Bar")
        val barComponent = bundled.getRawValue(from("/components/schemas/Bar"))
        barComponent.shouldNotBeNull()

        val refNested = getObject(bundled, "/components/schemas/Bar/properties/bar")
        refNested[$$"$ref"].shouldBe("#/components/schemas/Bar2")
        val componentNested = bundled.getRawValue(from("/components/schemas/Bar2"))
        componentNested.shouldNotBeNull()
    }

    withData(
        mapOf(
            "does not override existing component, 30" to Data("/bundle-ref-components/openapi30.yaml", ::bundle30),
            "does not override existing component, 31" to Data("/bundle-ref-components/openapi31.yaml", ::bundle31),
            "does not override existing component, 32" to Data("/bundle-ref-components/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val foo = bundled.getRawValue(from("/components/schemas/Foo"))
        foo.shouldNotBeNull()

        val existing = bundled.getRawValue(from("/components/schemas/Bar"))
        existing.shouldNotBeNull()
    }

    withData(
        mapOf(
            $$"bundle media type $ref, 32" to Data("/bundle-ref-mediatype/openapi32.yaml", ::bundle32)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val requestBody = getObject(bundled, "/paths/~1foo/get/requestBody/content/application~1json")
        requestBody[$$"$ref"].shouldBe("#/components/mediaTypes/Foo")
        getObject(bundled, "/components/mediaTypes/Foo").shouldNotBeNull()

        val response = getObject(bundled, "/paths/~1foo/get/responses/200/content/application~1json")
        response[$$"$ref"].shouldBe("#/components/mediaTypes/Bar")
        getObject(bundled, "/components/mediaTypes/Bar").shouldNotBeNull()
    }

   // conflict if name of bundled component is already used
})
