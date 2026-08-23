plugins {
    base
    alias(build.plugins.kotlin.jvm) apply false
    alias(build.plugins.openapiprocessor.publish)
    alias(build.plugins.openapiprocessor.bump)
    id("openapiparser.versions")
    id("test-report-aggregation")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

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

bump {
   toml = layout.projectDirectory.file("./gradle/libs.versions.toml")
   sectionKey = "versions"
   versionKey = "openapiparser"
}
