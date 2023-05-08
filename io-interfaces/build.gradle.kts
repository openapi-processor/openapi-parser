plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("io-interfaces")
                description.set("OpenAPI Processor Interfaces")
            }
        }
    }
}
