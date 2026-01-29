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
