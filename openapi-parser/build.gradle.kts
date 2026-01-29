plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
    alias(libs.plugins.sonar)
}

dependencies {
    api(project(":json-schema-validator"))
    api(project(":io-interfaces"))
    compileOnly(libs.jsonpath)
    implementation(libs.slf4j)

    testImplementation(project(":io-jackson"))
    testImplementation(project(":io-snakeyaml"))
    testImplementation(project(":memory-protocol"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
    testImplementation(libs.jsonpath)
    testImplementation(libs.logback)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
//                name.set("openapi-parser")
                description = "OpenAPI 3.2 - 3.0 parser"
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
