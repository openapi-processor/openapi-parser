plugins {
    base
    alias(libs.plugins.jacoco)
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
