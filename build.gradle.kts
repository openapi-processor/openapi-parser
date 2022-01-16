plugins {
//    java
//    id("org.barfuin.gradle.jacocolog") version "2.0.0"
    alias(libs.plugins.jacoco)
    alias(libs.plugins.nexus)
//    id("io.github.gradle-nexus.publish-plugin")  version "1.1.0"
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
