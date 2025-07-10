plugins {
    java
    alias(libs.plugins.jacoco)
    alias(libs.plugins.nexus)
    id("openapi-parser.publish-central")
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

extra["publishUser"] = buildProperty("PUBLISH_USER")
extra["publishKey"] = buildProperty("PUBLISH_KEY")
val publishUser: String by extra
val publishKey: String by extra

centralPublishing {
    username = publishUser
    password = publishKey
    stagingDir = layout.buildDirectory.dir("central")
}

nexusPublishing {
    this.repositories {
        sonatype {
            username.set(publishUser)
            password.set(publishKey)

            nexusUrl = uri("https://ossrh-staging-api.central.sonatype.com/service/local/")
            snapshotRepositoryUrl = uri("https://central.sonatype.com/repository/maven-snapshots/")
        }
    }
}
