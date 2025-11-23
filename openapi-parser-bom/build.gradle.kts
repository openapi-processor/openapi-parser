plugins {
    `java-platform`
    id("openapiparser.publish")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

dependencies {
    constraints {
        api(project(":openapi-parser"))
        api(project(":json-schema-validator"))
        api(project(":io-jackson"))
        api(project(":io-jackson3"))
        api(project(":io-snakeyaml"))
        api(project(":io-interfaces"))
    }
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "openapi-parser bom"
                description = "OpenAPI Parser Platform BOM"
                version = libs.versions.openapiparser.get()
            }
        }
    }
}
