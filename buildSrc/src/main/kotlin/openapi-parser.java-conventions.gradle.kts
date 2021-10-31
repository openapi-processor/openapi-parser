import net.ltgt.gradle.errorprone.errorprone
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    id("net.ltgt.errorprone")
    //id("org.unbroken-dome.test-sets")
    //id("com.github.ben-manes.versions")
}

group = "io.openapiprocessor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor ("com.uber.nullaway:nullaway:0.9.2")
    errorprone("com.google.errorprone:error_prone_core:2.9.0")

    testImplementation (platform("org.junit:junit-bom:5.7.2"))
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.mockk:mockk:1.12.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)

    options.errorprone {
        option("NullAway:AnnotatedPackages", "io.openapiparser")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
