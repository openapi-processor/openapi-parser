import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.accessors.dm.LibrariesForLibs

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
    compileOnly(libs.checkerq)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.datatest)
    testImplementation(libs.mockk)
    testCompileOnly(libs.checkerq)
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.build.jdk.get()))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.build.jdk.get()))
    }
}

//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.test.jdk.get()))
    })

    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<JavaExec>().configureEach {
    args("-ea")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
//        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

configure<org.checkerframework.gradle.plugin.CheckerFrameworkExtension> {
//    skipCheckerFramework = true
//    excludeTests = true
    extraJavacArgs = listOf("-Awarns")

    checkers = listOf(
        "org.checkerframework.checker.nullness.NullnessChecker",
//        "org.checkerframework.checker.interning.InterningChecker",
//        "org.checkerframework.checker.resourceleak.ResourceLeakChecker",
//        "org.checkerframework.checker.index.IndexChecker"
    )
}





tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }

    outputFormatter {
        exceeded.dependencies.removeIf { d -> ignore.contains("${d.group}:${d.name}") }

        val plainTextReporter = PlainTextReporter(
            project,
            revision,
            gradleReleaseChannel
        )
        plainTextReporter.write(System.out, this)
    }
}


fun String.isNonStable(): Boolean {
    val nonStable = listOf(
        ".M[0-9]+$",
        ".RC[0-9]*$",
        ".alpha[0-9]+$",
        ".beta[0-9]+$",
    )

    for (n in nonStable) {
       if (this.contains("(?i)$n".toRegex())) {
           return true
       }
    }

    return false
}

val ignore = listOf(
    "org.checkerframework:jdk8"
)
