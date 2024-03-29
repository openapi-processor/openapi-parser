plugins {
    `java-platform`
    id("openapi-parser.publish-conventions")
}

dependencies {
    constraints {
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
                name.set("json schema validator bom")
                description.set("JSON Schema Validator Platform BOM")
            }
        }
    }
}
