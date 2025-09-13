plugins {
    base
    alias(libs.plugins.jacoco)
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
    stagingDir = layout.buildDirectory.dir("staging")
    deploymentDir = layout.buildDirectory.dir("deployment")
    deploymentName = "parser"
}

afterEvaluate {
    if (hasProperty("snapshot")) {
        tasks.named("publishAllReleasesToMavenCentral") {
            enabled = version.toString().endsWith("SNAPSHOT")
            if (!enabled) {
                logger.warn("skipping publishing because version is not a snapshot.")
            }
        }
    }
}
