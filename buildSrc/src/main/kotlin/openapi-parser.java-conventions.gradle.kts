plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
//    id("org.unbroken-dome.test-sets")
//    id("com.github.ben-manes.versions")
}

group = "io.openapiprocessor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-params:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.mockk:mockk:1.12.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine") // :5.7.2
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
