@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.accessors.dm.LibrariesForBuild

plugins {
    java
    jacoco
    groovy
    kotlin
    id("org.barfuin.gradle.jacocolog")
}

// see buildSrc/build.gradle.kts
val libs = the<LibrariesForLibs>()
val build = the<LibrariesForBuild>()

testing {
    suites {
        getByName<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}

dependencies {
    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.table)
    testImplementation(libs.mockk)
    testCompileOnly(libs.checkerq)
}

tasks.withType<Test>().configureEach {
    jvmArgs("-Xshare:off")
}

jacoco {
    toolVersion = build.versions.jacoco.get()
}

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
        //html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}
