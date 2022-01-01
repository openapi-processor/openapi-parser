/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.openapiparser.support.buildObjectObsolete

fun callback(content: String = "{}"): Callback {
    return buildObjectObsolete(Callback::class.java, content)
}

fun components(content: String = "{}"): Components {
    return buildObjectObsolete(Components::class.java, content)
}

fun contact(content: String = "{}"): Contact {
    return buildObjectObsolete(Contact::class.java, content)
}

fun discriminator(content: String = "{}"): Discriminator {
    return buildObjectObsolete(Discriminator::class.java, content)
}

fun encoding(content: String = "{}"): Encoding {
    return buildObjectObsolete(Encoding::class.java, content)
}

fun example(content: String = "{}"): Example {
    return buildObjectObsolete(Example::class.java, content)
}

fun externalDocumentation(content: String = "{}"): ExternalDocumentation {
    return buildObjectObsolete(ExternalDocumentation::class.java, content)
}

fun header(content: String = "{}"): Header {
    return buildObjectObsolete(Header::class.java, content)
}

fun info(content: String = "{}"): Info {
    return buildObjectObsolete(Info::class.java, content)
}

fun license(content: String = "{}"): License {
    return buildObjectObsolete(License::class.java, content)
}

fun mediaType(content: String = "{}"): MediaType {
    return buildObjectObsolete(MediaType::class.java, content)
}

fun openapi(content: String = "{}"): OpenApi {
    return buildObjectObsolete(OpenApi::class.java, content)
}

fun operation(content: String = "{}"): Operation {
    return buildObjectObsolete(Operation::class.java, content)
}

fun pathItem(content: String = "{}"): PathItem {
    return buildObjectObsolete(PathItem::class.java, content)
}

fun paths(content: String = "{}"): Paths {
    return buildObjectObsolete(Paths::class.java, content)
}

fun parameter(content: String = "{}"): Parameter {
    return buildObjectObsolete(Parameter::class.java, content)
}

fun requestBody(content: String = "{}"): RequestBody {
    return buildObjectObsolete(RequestBody::class.java, content)
}

fun response(content: String = "{}"): Response {
    return buildObjectObsolete(Response::class.java, content)
}

fun responses(content: String = "{}"): Responses {
    return buildObjectObsolete(Responses::class.java, content)
}

fun schema(content: String = "{}"): Schema {
    return buildObjectObsolete(Schema::class.java, content)
}

fun server(content: String = "{}"): Server {
    return buildObjectObsolete(Server::class.java, content)
}

fun serverVariable(content: String = "{}"): ServerVariable {
    return buildObjectObsolete(ServerVariable::class.java, content)
}

fun xml(content: String = "{}"): Xml {
    return buildObjectObsolete(Xml::class.java, content)
}
