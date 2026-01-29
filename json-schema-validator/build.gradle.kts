plugins {
    id("openapiparser.test")
    id("openapiparser.library")
    id("openapiparser.publish")
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
}

val generateTestManifest by tasks.registering(GenerateManifestTask::class) {
    val resourcesPath = "src/test/resources"
    val testsPath = "$resourcesPath/suites/JSON-Schema-Test-Suite/tests"

    baseResourcesDir.set(layout.projectDirectory.dir(resourcesPath))
    testFiles.from(layout.projectDirectory.dir(testsPath).asFileTree)
    outputDir.set(temporaryDir)
}

sourceSets {
  test {
    resources {
      srcDir(generateTestManifest.map { it.temporaryDir })
    }
  }
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name = "json-schema-validator"
                description = "OpenAPI Parser JSON-Schema Validator"
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
