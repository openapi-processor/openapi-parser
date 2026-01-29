plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
}

dependencies {
    implementation(project(":io-interfaces"))
    implementation(libs.snakeyaml)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "io-snakeyaml"
                description = "OpenAPI Processor snakeyaml YAML Converter"
            }
        }
    }
}
