plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
    alias(libs.plugins.sonar)
}

dependencies {
    api(project(":json-schema-validator"))
    api(project(":io-interfaces"))

    testImplementation(project(":io-jackson"))
    testImplementation(project(":io-snakeyaml"))
    testImplementation(project(":memory-protocol"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
    testImplementation(libs.logback)

    constraints {
        implementation(libs.slf4j) { because("avoid multiple versions") }
        testImplementation(libs.slf4j) { because("avoid multiple versions") }
    }
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

sonar {
  properties {
    property("sonar.projectKey", "openapi-processor_openapi-parser-openapi-parser")
    property("sonar.organization", "openapi-processor")
    property("sonar.host.url", "https://sonarcloud.io")
//    property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
  }
}

tasks {
    // groovy tests depend on kotlin tests
//    compileTestGroovy {
//        dependsOn(compileTestKotlin)
//        classpath += files(compileTestKotlin)
//    }
}
