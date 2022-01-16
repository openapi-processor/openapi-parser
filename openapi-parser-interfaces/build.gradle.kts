plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser-interfaces")
                description.set("OpenAPI Parser Interfaces")
            }
        }
    }
}
