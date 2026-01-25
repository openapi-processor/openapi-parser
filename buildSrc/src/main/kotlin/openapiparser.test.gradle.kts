@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    groovy
    kotlin
}

// see buildSrc/build.gradle.kts
val libs = the<LibrariesForLibs>()

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
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
