plugins {
    base
    alias(libs.plugins.jacoco)
    id("io.openapiprocessor.build.plugin.publish-central")
}

repositories {
    mavenCentral()
}

// check
tasks.named("build") {
    dependsOn ("jacocoLogAggregatedCoverage")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

publishProcessor {
    stagingDir = layout.buildDirectory.dir("staging")
    deploymentDir = layout.buildDirectory.dir("deployment")
    publish = false
}
