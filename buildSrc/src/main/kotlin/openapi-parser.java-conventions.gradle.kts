import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    jacoco
    kotlin("jvm")

    id( "org.checkerframework")
    id("com.github.ben-manes.versions")
}

repositories {
    mavenCentral()
}

dependencies {
    checkerFramework(libs("checker"))

    testImplementation(platform(libs("kotest.bom")))
    testImplementation(libs("kotest.runner"))
    testImplementation(libs("kotest.datatest"))
    testImplementation(libs("mockk"))

//    testImplementation (platform("org.junit:junit-bom:5.7.2"))
//    testImplementation ("org.junit.jupiter:junit-jupiter-api")
//    testImplementation ("org.junit.jupiter:junit-jupiter-params")
//    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
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
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

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

@Suppress("UnstableApiUsage")
fun libs(dependency: String): Provider<MinimalExternalModuleDependency> {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    return libs.findDependency(dependency).orElseThrow {
        Exception("can't find dependency: $dependency")
    }
}
