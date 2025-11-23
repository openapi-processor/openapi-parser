plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
}

dependencies {
    implementation(project(":io-interfaces"))

    implementation(platform(libs.jackson3.bom))
    implementation(libs.jackson3.databind)
    implementation(libs.jackson3.yaml)

    constraints {
        implementation(libs.slf4j) { because("avoid multiple versions") }
        testImplementation(libs.slf4j) { because("avoid multiple versions") }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "io-jackson3"
                description = "OpenAPI Processor Jackson 3 JSON/YAML Converter"
            }
        }
    }
}
