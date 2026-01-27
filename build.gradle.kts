plugins {
    base
    id("io.openapiprocessor.build.plugin.publish")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

repositories {
    mavenCentral()
}

// check
tasks.named("build") {
    dependsOn ("jacocoLogAggregatedCoverage")
}

publishingCentral {
    aggregateSubProjects = true
    stagingDir = layout.buildDirectory.dir("staging")
    deploymentDir = layout.buildDirectory.dir("deployment")
    deploymentName = "parser"
}
