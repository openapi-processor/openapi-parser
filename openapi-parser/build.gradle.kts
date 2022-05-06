plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

dependencies {
    implementation(project(":openapi-parser-validator"))
    implementation(project(":openapi-parser-interfaces"))

    testImplementation(project(":openapi-parser-snakeyaml"))
    testImplementation(project(":openapi-parser-jackson"))
    testImplementation(project(":memory-protocol"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
    testImplementation(libs.logback)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser")
                description.set("OpenAPI 3.0/3.1 parser")
            }
        }
    }
}

tasks {
    // groovy tests depend on kotlin tests
//    compileTestGroovy {
//        dependsOn(compileTestKotlin)
//        classpath += files(compileTestKotlin)
//    }
}
