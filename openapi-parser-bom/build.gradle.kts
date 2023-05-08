plugins {
    `java-platform`
    id("openapi-parser.publish-conventions")
}

dependencies {
    constraints {
        api(project(":openapi-parser"))
        api(project(":json-schema-validator"))
        api(project(":io-jackson"))
        api(project(":io-snakeyaml"))
        api(project(":io-interfaces"))
    }
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser bom")
                description.set("OpenAPI Parser Platform BOM")
            }
        }
    }
}
