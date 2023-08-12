plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":io-interfaces"))

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.yaml)

    constraints {
        implementation(libs.slf4j) { because("avoid multiple versions") }
        testImplementation(libs.slf4j) { because("avoid multiple versions") }
    }
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("io-jackson")
                description.set("OpenAPI Processor Jackson JSON/YAML Converter")
            }
        }
    }
}
