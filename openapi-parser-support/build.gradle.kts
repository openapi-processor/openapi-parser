plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":openapi-parser-interfaces"))

    testImplementation(project(":openapi-parser-memory"))
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser-support")
                description.set("OpenAPI Parser Shared Code")
            }
        }
    }
}
