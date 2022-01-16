plugins {
    `java-platform`
    id("openapi-parser.publish-conventions")
}

dependencies {
    constraints {
        api(project(":openapi-parser"))
        api(project(":openapi-parser-interfaces"))
        api(project(":openapi-parser-validator"))
        api(project(":openapi-parser-jackson"))
        api(project(":openapi-parser-support"))
        api(project(":openapi-parser-memory"))
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
