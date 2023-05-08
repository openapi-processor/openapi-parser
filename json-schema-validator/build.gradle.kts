plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":io-interfaces"))
    implementation(libs.slf4j)

    testImplementation(project(":memory-protocol"))
    testImplementation(project(":openapi-parser-jackson"))
    testImplementation(project(":io-snakeyaml"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
    testImplementation(libs.logback)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser-validator")
                description.set("OpenAPI Parser JSON-Schema Validator")
            }
        }
    }
}
