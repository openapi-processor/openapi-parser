/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.openapiparser.support.buildObject

fun callback(content: String = "{}"): Callback {
    return buildObject(Callback::class.java, content)
}

fun components(content: String = "{}"): Components {
    return buildObject(Components::class.java, content)
}

fun contact(content: String = "{}"): Contact {
    return buildObject(Contact::class.java, content)
}

fun discriminator(content: String = "{}"): Discriminator {
    return buildObject(Discriminator::class.java, content)
}

fun encoding(content: String = "{}"): Encoding {
    return buildObject(Encoding::class.java, content)
}

fun example(content: String = "{}"): Example {
    return buildObject(Example::class.java, content)
}

fun externalDocumentation(content: String = "{}"): ExternalDocumentation {
    return buildObject(ExternalDocumentation::class.java, content)
}

fun header(content: String = "{}"): Header {
    return buildObject(Header::class.java, content)
}

fun info(content: String = "{}"): Info {
    return buildObject(Info::class.java, content)
}

fun license(content: String = "{}"): License {
    return buildObject(License::class.java, content)
}

fun mediaType(content: String = "{}"): MediaType {
    return buildObject(MediaType::class.java, content)
}

fun openapi(content: String = "{}"): OpenApi {
    return buildObject(OpenApi::class.java, content)
}

fun operation(content: String = "{}"): Operation {
    return buildObject(Operation::class.java, content)
}

fun pathItem(content: String = "{}"): PathItem {
    return buildObject(PathItem::class.java, content)
}

fun paths(content: String = "{}"): Paths {
    return buildObject(Paths::class.java, content)
}

fun parameter(content: String = "{}"): Parameter {
    return buildObject(Parameter::class.java, content)
}

fun requestBody(content: String = "{}"): RequestBody {
    return buildObject(RequestBody::class.java, content)
}

fun response(content: String = "{}"): Response {
    return buildObject(Response::class.java, content)
}

fun responses(content: String = "{}"): Responses {
    return buildObject(Responses::class.java, content)
}

fun schema(content: String = "{}"): Schema {
    return buildObject(Schema::class.java, content)
}

fun server(content: String = "{}"): Server {
    return buildObject(Server::class.java, content)
}

fun serverVariable(content: String = "{}"): ServerVariable {
    return buildObject(ServerVariable::class.java, content)
}

fun xml(content: String = "{}"): Xml {
    return buildObject(Xml::class.java, content)
}
