plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":io-interfaces"))

    implementation(libs.snakeyaml)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser-snakeyaml")
                description.set("OpenAPI Parser snakeyaml YAML Converter")
            }
        }
    }
}
