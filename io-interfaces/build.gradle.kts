plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "io-interfaces"
                description = "OpenAPI Processor Interfaces"
            }
        }
    }
}
