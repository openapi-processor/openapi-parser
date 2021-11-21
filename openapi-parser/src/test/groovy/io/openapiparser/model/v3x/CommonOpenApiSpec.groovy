package io.openapiparser.model.v3x

import spock.lang.Specification

import static io.openapiparser.model.v30.OpenApiSpecKt.openapi as openapi30
import static io.openapiparser.model.v31.OpenApiSpecKt.openapi as openapi31

class CommonOpenApiSpec extends Specification {

    void "gets openapi version"() {
        expect:
        source.'openapi' == result

        where:
        source                     | result
        openapi30 ("openapi: 3.0.3") | "3.0.3"
        openapi31 ("openapi: 3.1.0") | "3.1.0"
    }

}
