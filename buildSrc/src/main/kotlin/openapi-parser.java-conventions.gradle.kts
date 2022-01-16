@file:Suppress("UnstableApiUsage")

import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    jacoco
    kotlin("jvm")

    id("org.checkerframework")
    id("com.github.ben-manes.versions")
}

// see buildSrc/build.gradle catalog hack
val libs = the<LibrariesForLibs>()

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

repositories {
    mavenCentral()
}

dependencies {
    checkerFramework(libs.checker)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.datatest)
    testImplementation(libs.mockk)

//    testImplementation (platform("org.junit:junit-bom:5.7.2"))
//    testImplementation ("org.junit.jupiter:junit-jupiter-api")
//    testImplementation ("org.junit.jupiter:junit-jupiter-params")
//    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

/*
tasks.withType<JavaCompile>().configureEach {
    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    })
}

tasks.register<Test>("testsOn14") {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(14))
    })
}
 */


tasks.withType<JavaCompile>().configureEach {
//    javaCompiler.set(javaToolchains.compilerFor {
//        languageVersion.set(JavaLanguageVersion.of(8))
//    })
    options.release.set(8)
}

//tasks.withType<Test>().configureEach() {
//    javaLauncher.set(javaToolchains.launcherFor {
//        languageVersion.set(JavaLanguageVersion.of(11))
//    })
//}
//
//
//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions.jvmTarget = "11"
//}

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
//        xml.required.set(false)
//        csv.required.set(false)
//        html.required.set(false)
//        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

