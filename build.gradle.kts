@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

plugins {
    java
    alias(libs.plugins.jacoco)
    alias(libs.plugins.nexus)
}

repositories {
    mavenCentral()
}

// check
//tasks.named("build") {
//    dependsOn ("jacocoLogAggregatedCoverage")
//}


extra["publishUser"] = buildProperty("PUBLISH_USER")
extra["publishKey"] = buildProperty("PUBLISH_USER")
val publishUser: String by extra
val publishKey: String by extra

nexusPublishing {
    repositories {
        sonatype {
            username.set(publishUser)
            password.set(publishKey)
        }
    }
}
