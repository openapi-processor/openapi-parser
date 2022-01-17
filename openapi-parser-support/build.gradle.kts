plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":openapi-parser-interfaces"))

    testImplementation(project(":memory-protocol"))
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
