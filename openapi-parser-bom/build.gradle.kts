plugins {
    `java-platform`
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
