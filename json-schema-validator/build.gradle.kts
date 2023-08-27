plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
    alias(libs.plugins.sonar)
}

dependencies {
    implementation(project(":io-interfaces"))
    implementation(libs.slf4j)

    testImplementation(project(":io-snakeyaml"))
    testImplementation(project(":io-jackson"))
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
                name.set("json-schema-validator")
                description.set("OpenAPI Parser JSON-Schema Validator")
            }
        }
    }
}

sonar {
  properties {
    property("sonar.projectKey", "openapi-processor_openapi-parser-json-schema-validator")
    property("sonar.organization", "openapi-processor")
    property("sonar.host.url", "https://sonarcloud.io")
//    property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
  }
}
