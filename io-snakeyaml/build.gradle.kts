plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
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
                name.set("io-snakeyaml")
                description.set("OpenAPI Processor snakeyaml YAML Converter")
            }
        }
    }
}
