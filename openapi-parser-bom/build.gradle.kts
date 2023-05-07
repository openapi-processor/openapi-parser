plugins {
    `java-platform`
    id("openapi-parser.publish-conventions")
}

dependencies {
    constraints {
        api(project(":openapi-parser"))
        api(project(":openapi-parser-interfaces"))
        api(project(":openapi-parser-jackson"))
        api(project(":json-schema-validator"))
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
