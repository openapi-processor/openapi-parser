plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":openapi-parser-interfaces"))
    implementation(project(":openapi-parser-support"))

    testImplementation(project(":openapi-parser-jackson"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
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
