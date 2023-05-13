plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":io-interfaces"))
    implementation(libs.slf4j)

    testImplementation(project(":io-snakeyaml"))
    testImplementation(project(":io-jackson"))
    testImplementation(project(":memory-protocol"))
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
                name.set("json-schema-validator")
                description.set("OpenAPI Parser JSON-Schema Validator")
            }
        }
    }
}
