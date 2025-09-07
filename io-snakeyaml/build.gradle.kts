plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
}

dependencies {
    implementation(project(":io-interfaces"))
    implementation(libs.snakeyaml)

    constraints {
        implementation(libs.slf4j) { because("avoid multiple versions") }
        testImplementation(libs.slf4j) { because("avoid multiple versions") }
    }
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
