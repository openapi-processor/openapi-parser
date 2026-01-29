plugins {
    base
    alias(libs.plugins.jacocolog)
    id("test-report-aggregation")
    id("io.openapiprocessor.build.plugin.publish")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

repositories {
    mavenCentral()
}

dependencies {
    testReportAggregation(project(":io-jackson"))
    testReportAggregation(project(":io-jackson3"))
    testReportAggregation(project(":io-snakeyaml"))
    testReportAggregation(project(":json-schema-validator"))
    testReportAggregation(project(":openapi-parser"))
}

publishingCentral {
    aggregateSubProjects = true
    stagingDir = layout.buildDirectory.dir("staging")
    deploymentDir = layout.buildDirectory.dir("deployment")
    deploymentName = "parser"
}
