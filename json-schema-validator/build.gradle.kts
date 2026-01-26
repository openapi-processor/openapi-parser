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
    testImplementation(libs.classgraph)
    testImplementation(libs.logback)

    constraints {
        implementation(libs.slf4j) { because("avoid multiple versions") }
        testImplementation(libs.slf4j) { because("avoid multiple versions") }
    }
}

val generateTestManifest by tasks.registering {
    val resourceDir = layout.projectDirectory.dir("src/test/resources/suites/JSON-Schema-Test-Suite/tests")
    inputs.dir(resourceDir)
    outputs.dir(temporaryDir)

    val tests = resourceDir.asFileTree

    doLast {
        val paths = tests
            .map { file -> "/" + file.relativeTo(layout.projectDirectory.dir("src/test/resources").asFile).path }
            .sortedWith (compareBy<String> { path -> path.count { it.toString() == "/" } }.thenBy { path -> path })
        val outputDir = File(temporaryDir, "suites")
        outputDir.mkdirs()
        val outputFile = File(outputDir, "JSON-Schema-Test-Suite.txt")
        outputFile.writeText(paths.joinToString("\n"))
    }
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
