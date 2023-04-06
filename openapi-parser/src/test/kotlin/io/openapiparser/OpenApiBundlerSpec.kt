/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.Types.asObject
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.*
import io.openapiparser.schema.JsonPointer.from
import io.openapiparser.snakeyaml.SnakeYamlConverter

class OpenApiBundlerSpec : StringSpec({

    fun resolve(documentUri: String): ResolverResult {
        val documents = DocumentStore()
        val loader = DocumentLoader(UriReader(), SnakeYamlConverter())
        val resolver = Resolver(documents, loader, Resolver.Settings(SchemaVersion.Draft4))
        return resolver.resolve(documentUri)
    }

    fun bundle30(result: ResolverResult): Bucket {
        val context = Context(result.scope, result.registry)
        val bucket = Bucket(result.scope, asObject(result.document))
        val api = OpenApiResult30(context, bucket, result.documents)
        val bundle = api.bundle()
        return Bucket.toBucket(result.scope, bundle)!!
    }

    fun getObject(bucket: Bucket, jsonPointer: String): Map<String, Any> {
        return asObject(bucket.getRawValue(from(jsonPointer))!!.value)
    }

    "bundle schema \$ref" {
        val result = resolve("/bundle-ref-schema/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/responses/200/content/application~1json/schema")
        ref["\$ref"].shouldBe("#/components/schemas/Foo")

        val component = bundle.getRawValue(from("/components/schemas/Foo"))
        component.shouldNotBeNull()
    }

    "bundle response \$ref" {
        val result = resolve("/bundle-ref-response/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/responses/200")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/responses/Foo")

        val component = getObject(bundle, "/components/responses/Foo")
        component.shouldNotBeNull()
    }

    "bundle parameter \$ref" {
        val result = resolve("/bundle-ref-parameter/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/parameters/0")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/parameters/Foo")

        val component = getObject(bundle, "/components/parameters/Foo")
        component.shouldNotBeNull()
    }

    "bundle example \$ref" {
        val result = resolve("/bundle-ref-example/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/parameters/0/examples/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/examples/Foo")

        val component = getObject(bundle, "/components/examples/Foo")
        component.shouldNotBeNull()
    }

    "bundle request body \$ref" {
        val result = resolve("/bundle-ref-request-body/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/requestBody")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/requestBodies/Foo")

        val component = getObject(bundle, "/components/requestBodies/Foo")
        component.shouldNotBeNull()
    }

    "bundle header \$ref" {
        val result = resolve("/bundle-ref-header/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/responses/204/headers/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/headers/Foo")

        val component = getObject(bundle, "/components/headers/Foo")
        component.shouldNotBeNull()
    }

    "bundle security scheme \$ref" {
        val result = resolve("/bundle-ref-security-scheme/openapi30.yaml")
        val bundle = bundle30(result)

        val scheme = getObject(bundle, "/components/securitySchemes/jwt")
        scheme.containsKey(Keywords.REF).shouldBeFalse()
        scheme.containsKey("type").shouldBeTrue()
    }

    "bundle link \$ref" {
        val result = resolve("/bundle-ref-link/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/responses/204/links/foo")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/links/Foo")

        val component = getObject(bundle, "/components/links/Foo")
        component.shouldNotBeNull()
    }

    "bundle callback \$ref" {
        val result = resolve("/bundle-ref-callback/openapi30.yaml")
        val bundle = bundle30(result)

        val ref = getObject(bundle, "/paths/~1foo/get/callbacks/\$url")
        ref.size shouldBe 1
        ref["\$ref"].shouldBe("#/components/callbacks/Foo")

        val component = getObject(bundle, "/components/callbacks/Foo")
        component.shouldNotBeNull()
    }

    "bundle path \$ref" {
        val result = resolve("/bundle-ref-path-item/openapi30.yaml")
        val bundle = bundle30(result)

        val pathItem = getObject(bundle, "/paths/~1foo")
        pathItem.containsKey(Keywords.REF).shouldBeFalse()
        pathItem.containsKey("get").shouldBeTrue()
    }

    "does not override existing component" {
        val result = resolve("/bundle-ref-components/openapi30.yaml")
        val bundle = bundle30(result)

        val bundled = bundle.getRawValue(from("/components/schemas/Foo"))
        bundled.shouldNotBeNull()

        val existing = bundle.getRawValue(from("/components/schemas/Bar"))
        existing.shouldNotBeNull()
    }

    // should not modify in-document refs
    // conflict if name of bundled component is already used
})
