import org.gradle.accessors.dm.LibrariesForBuild
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin
    `java-library`
    id("org.checkerframework")
}

// see build.gradle.kts
val libs = the<LibrariesForLibs>()
val build = the<LibrariesForBuild>()

group = "io.openapiprocessor"
version = libs.versions.openapiparser.get()
println("version: $version")

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(build.versions.build.jdk.get()))
    }
}

tasks.javadoc {
    options {
        // don't report missing Javadoc warnings
        (this as CoreJavadocOptions).addBooleanOption("Xdoclint:-missing", true)
    }
}

kotlin {
    jvmToolchain(build.versions.build.jdk.get().toInt())
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots")
        mavenContent {
            snapshotsOnly()
        }
    }
}

checkerFramework {
  version = libs.versions.checker
}

configure<org.checkerframework.plugin.gradle.CheckerFrameworkExtension> {
//    skipCheckerFramework = true
//    excludeTests = true

    extraJavacArgs = listOf(
        "-Awarns"
    )

    checkers = listOf(
        "org.checkerframework.checker.nullness.NullnessChecker",
//        "org.checkerframework.checker.interning.InterningChecker",
//        "org.checkerframework.checker.resourceleak.ResourceLeakChecker",
//        "org.checkerframework.checker.index.IndexChecker"
    )
}

tasks.withType<Test>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(build.versions.test.jdk.get()))
    })

    finalizedBy(tasks.named("jacocoTestReport"))
}
