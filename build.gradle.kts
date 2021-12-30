plugins {
    java
    id("org.barfuin.gradle.jacocolog") version "2.0.0"
}

repositories {
    mavenCentral()
}

tasks.named("check") {
    dependsOn ("jacocoLogAggregatedCoverage")
}

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")
