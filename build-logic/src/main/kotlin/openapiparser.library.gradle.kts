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

dependencies {
    checkerFramework(libs.checker)
    api(libs.checkerq)
}

configure<org.checkerframework.gradle.plugin.CheckerFrameworkExtension> {
//    skipCheckerFramework = true
//    excludeTests = true

    extraJavacArgs = listOf(
        "-Awarns",
        "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED"
    )

    checkers = listOf(
        "org.checkerframework.checker.nullness.NullnessChecker",
//        "org.checkerframework.checker.interning.InterningChecker",
//        "org.checkerframework.checker.resourceleak.ResourceLeakChecker",
//        "org.checkerframework.checker.index.IndexChecker"
    )
}

tasks.withType<Test>().configureEach {
    jvmArgs(listOf(
        "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
    ))

    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(build.versions.test.jdk.get()))
    })

    finalizedBy(tasks.named("jacocoTestReport"))
}
