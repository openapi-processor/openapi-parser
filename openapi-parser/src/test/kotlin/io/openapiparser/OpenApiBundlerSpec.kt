/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.Types.asObject
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.JsonPointer.from
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import io.openapiprocessor.jsonschema.schema.*

class OpenApiBundlerSpec : StringSpec({

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

    fun getObject(bucket: Bucket, jsonPointer: String): Map<String, Any> {
        return asObject(bucket.getRawValue(from(jsonPointer))!!.value)
    }

    data class Data(val api: String, val bundle: (result: ResolverResult) -> Bucket)

    withData(
        mapOf(
            "bundle handles \$ref loop, 30" to Data("/bundle-ref-loop/openapi30.yaml", ::bundle30),
            "bundle handles \$ref loop, 31" to Data("/bundle-ref-loop/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1self-reference/get/responses/200/content/application~1json/schema")
        ref["\$ref"].shouldBe("#/components/schemas/Self")

        val schema = getObject(bundled, "/components/schemas/Self/properties/self")
        schema["\$ref"].shouldBe("#/components/schemas/Self")
    }

    withData(
        mapOf(
            "bundle schema \$ref, 30" to Data("/bundle-ref-schema/openapi30.yaml", ::bundle30),
            "bundle schema \$ref, 31" to Data("/bundle-ref-schema/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200/content/application~1json/schema")
        ref["\$ref"].shouldBe("#/components/schemas/Foo")

        val component = bundled.getRawValue(from("/components/schemas/Foo"))
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle response \$ref, 30" to Data("/bundle-ref-response/openapi30.yaml", ::bundle30),
            "bundle response \$ref, 31" to Data("/bundle-ref-response/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/responses/Foo")

        val component = getObject(bundled, "/components/responses/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle parameter \$ref, 30" to Data("/bundle-ref-parameter/openapi30.yaml", ::bundle30),
            "bundle parameter \$ref, 31" to Data("/bundle-ref-parameter/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/parameters/0")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/parameters/Foo")

        val component = getObject(bundled, "/components/parameters/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle example \$ref, 30" to Data("/bundle-ref-example/openapi30.yaml", ::bundle30),
            "bundle example \$ref, 31" to Data("/bundle-ref-example/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/parameters/0/examples/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/examples/Foo")

        val component = getObject(bundled, "/components/examples/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle request body \$ref, 30" to Data("/bundle-ref-request-body/openapi30.yaml", ::bundle30),
            "bundle request body \$ref, 31" to Data("/bundle-ref-request-body/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/requestBody")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/requestBodies/Foo")

        val component = getObject(bundled, "/components/requestBodies/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle header \$ref, 30" to Data("/bundle-ref-header/openapi30.yaml", ::bundle30),
            "bundle header \$ref, 31" to Data("/bundle-ref-header/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/204/headers/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/headers/Foo")

        val component = getObject(bundled, "/components/headers/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle security scheme \$ref, 30" to Data("/bundle-ref-security-scheme/openapi30.yaml", ::bundle30),
            "bundle security scheme \$ref, 31" to Data("/bundle-ref-security-scheme/openapi31.yaml", ::bundle31)
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
            "bundle link \$ref, 30" to Data("/bundle-ref-link/openapi30.yaml", ::bundle30),
            "bundle link \$ref, 31" to Data("/bundle-ref-link/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/responses/204/links/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/links/Foo")

        val component = getObject(bundled, "/components/links/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle callback \$ref, 30" to Data("/bundle-ref-callback/openapi30.yaml", ::bundle30),
            "bundle callback \$ref, 31" to Data("/bundle-ref-callback/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val ref = getObject(bundled, "/paths/~1foo/get/callbacks/\$url")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/callbacks/Foo")

        val component = getObject(bundled, "/components/callbacks/Foo")
        component.shouldNotBeNull()
    }

    withData(
        mapOf(
            "bundle path \$ref, 30" to Data("/bundle-ref-path-item/openapi30.yaml", ::bundle30),
            "bundle path \$ref, 31" to Data("/bundle-ref-path-item/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val pathItem = getObject(bundled, "/paths/~1foo")
        pathItem.containsKey(Keywords.REF).shouldBeFalse()
        pathItem.containsKey("get").shouldBeTrue()
    }

    withData(
        mapOf(
            "bundle nested \$ref, 30" to Data("/bundle-ref-nested/openapi30.yaml", ::bundle30),
            "bundle nested \$ref, 31" to Data("/bundle-ref-nested/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val pathItem = getObject(bundled, "/paths/~1foo")
        pathItem.containsKey(Keywords.REF).shouldBeFalse()
        pathItem.containsKey("get").shouldBeTrue()

        val ref = getObject(bundled, "/paths/~1foo/get/responses/200/content/application~1json/schema")
        ref["\$ref"].shouldBe("#/components/schemas/Bar")
        val component = bundled.getRawValue(from("/components/schemas/Bar"))
        component.shouldNotBeNull()

        val refNested = getObject(bundled, "/components/schemas/Bar/properties/bar")
        refNested["\$ref"].shouldBe("#/components/schemas/Bar2")
        val componentNested = bundled.getRawValue(from("/components/schemas/Bar2"))
        componentNested.shouldNotBeNull()
    }

    withData(
        mapOf(
            "does not override existing component, 30" to Data("/bundle-ref-components/openapi30.yaml", ::bundle30),
            "does not override existing component, 31" to Data("/bundle-ref-components/openapi31.yaml", ::bundle31)
        )
    ) { (api, bundle) ->
        val result = resolve(api)
        val bundled = bundle(result)

        val foo = bundled.getRawValue(from("/components/schemas/Foo"))
        foo.shouldNotBeNull()

        val existing = bundled.getRawValue(from("/components/schemas/Bar"))
        existing.shouldNotBeNull()
    }

    // should not modify in-document refs
    // conflict if name of bundled component is already used
})
