plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
}

dependencies {
    implementation(project(":io-interfaces"))

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.yaml)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "io-jackson"
                description = "OpenAPI Processor Jackson JSON/YAML Converter"
            }
        }
    }
}
